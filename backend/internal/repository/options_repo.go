package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"strings"
	"verarti/internal/domain"
	"verarti/models"
	"verarti/pkg/database"
)

type OptionPostgres struct {
	db *sqlx.DB
}

func NewOptionPostgres(db *sqlx.DB) *OptionPostgres {
	return &OptionPostgres{db: db}
}

func (r *OptionPostgres) CreateOption(option models.Option) (int, error) {
	var id int
	query := fmt.Sprintf("INSERT INTO %s (name, description, duration, price)"+
		"VALUES ($1, $2, $3, $4) RETURNING id", database.OptionTable)
	row := r.db.QueryRow(query, option.Name, option.Description, option.Duration, option.Price)
	if err := row.Scan(&id); err != nil {
		var pqErr *pq.Error
		if errors.As(err, &pqErr) {
			if pqErr.Code == "23505" {
				if strings.Contains(pqErr.Message, "name") {
					return 0, domain.NewErrorResponse(409, "option with this name already exists")
				}
			}
		}

		return 0, err
	}

	return id, nil
}

func (r *OptionPostgres) GetAllOptions() ([]models.Option, error) {
	var options []models.Option
	query := fmt.Sprintf("SELECT * FROM %s", database.OptionTable)
	err := r.db.Select(&options, query)

	if len(options) == 0 {
		return []models.Option{}, err
	}

	return options, err
}

func (r *OptionPostgres) GetOptionsByMasterId(masterId int) ([]models.Option, error) {
	var options []models.Option
	query := fmt.Sprintf("SELECT op.id, op.name, op.description, op.duration, op.price "+
		" FROM %s op INNER JOIN %s us_op on op.id = us_op.option_id"+
		" WHERE us_op.users_id = $1", database.OptionTable, database.UsersOptionTable)
	err := r.db.Select(&options, query, masterId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, nil
		}

		return nil, err
	}

	if len(options) == 0 {
		return []models.Option{}, err
	}

	return options, err
}

func (r *OptionPostgres) GetOptionById(optionId int) (models.Option, error) {
	var option models.Option
	query := fmt.Sprintf("SELECT * FROM %s WHERE id = $1", database.OptionTable)
	err := r.db.Get(&option, query, optionId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return models.Option{}, domain.NewErrorResponse(404, fmt.Sprintf("option with this id = %d not found", optionId))
		}

		return models.Option{}, err
	}

	return option, err
}

func (r *OptionPostgres) UpdateOption(option models.OptionUpdate, optionId int) error {
	var id int
	queryGetOption := fmt.Sprintf("SELECT id FROM %s WHERE id = $1", database.OptionTable)
	err := r.db.Get(&id, queryGetOption, optionId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return domain.NewErrorResponse(404, fmt.Sprintf("option with this id = %d not found", optionId))
		}

		return err
	}

	setValues := make([]string, 0)
	args := make([]interface{}, 0)
	argId := 1

	if option.Name != "" {
		setValues = append(setValues, fmt.Sprintf("name=$%d", argId))
		args = append(args, option.Name)
		argId++
	}

	if option.Description != "" {
		setValues = append(setValues, fmt.Sprintf("description=$%d", argId))
		args = append(args, option.Description)
		argId++
	}

	if option.Duration != "" {
		setValues = append(setValues, fmt.Sprintf("duration=$%d", argId))
		args = append(args, option.Duration)
		argId++
	}

	if option.Price != 0 {
		setValues = append(setValues, fmt.Sprintf("price=$%d", argId))
		args = append(args, option.Price)
		argId++
	}

	setQuery := strings.Join(setValues, ", ")

	if setQuery == "" {
		return domain.NewErrorResponse(400, "invalid input body: it is empty")
	}

	query := fmt.Sprintf("UPDATE %s SET %s WHERE id = $%d",
		database.OptionTable, setQuery, argId)
	args = append(args, optionId)

	_, err = r.db.Exec(query, args...)
	return err
}

func (r *OptionPostgres) DeleteOption(optionId int) error {
	var id int
	queryGetOption := fmt.Sprintf("SELECT id FROM %s WHERE id = $1", database.OptionTable)
	err := r.db.Get(&id, queryGetOption, optionId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return domain.NewErrorResponse(404, fmt.Sprintf("option with this id = %d not found", optionId))
		}

		return err
	}

	query := fmt.Sprintf(`DELETE FROM %s WHERE id = $1`, database.OptionTable)
	_, err = r.db.Exec(query, optionId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return domain.NewErrorResponse(404, "option not found")
		}

		return err
	}

	return nil
}

func (r *OptionPostgres) AddOptionForMaster(masterId, optionId int) (int, error) {
	var id int

	queryAddOption := fmt.Sprintf("INSERT INTO %s (users_id, option_id)"+
		"VALUES ($1, $2) RETURNING id", database.UsersOptionTable)
	row := r.db.QueryRow(queryAddOption, masterId, optionId)
	if err := row.Scan(&id); err != nil {
		return 0, err
	}

	return id, nil
}

func (r *OptionPostgres) RemoveOptionFromTheMaster(optionId, masterId int) error {
	queryDeleteOption := fmt.Sprintf("DELETE FROM %s WHERE users_id = $1 AND option_id = $2", database.UsersOptionTable)
	_, err := r.db.Exec(queryDeleteOption, masterId, optionId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil
		}

		return err
	}

	return nil
}

func (r *OptionPostgres) CheckingOptionsExistence(optionIds []int) error {
	var exists int

	for _, optionId := range optionIds {
		queryGetOption := fmt.Sprintf(`SELECT 1 FROM %s WHERE id = $1`, database.OptionTable)
		err := r.db.Get(&exists, queryGetOption, optionId)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return domain.NewErrorResponse(404, fmt.Sprintf("option with this id = %d was not found", optionId))
			}

			return err
		}
	}

	return nil
}

func (r *OptionPostgres) CheckingActiveOptionExistenceForAllUsers(optionId int) (bool, error) {
	var exists bool

	queryGetOption := fmt.Sprintf(`SELECT EXISTS(SELECT 1 FROM %s AS app_opt
		INNER JOIN %s AS app ON app.id = app_opt.master_appointment_id 
		WHERE app_opt.option_id = $1 AND (
                app.date > CURRENT_DATE OR 
                (app.date = CURRENT_DATE AND app.start_time::time >= CURRENT_TIME)
            ))`, database.MasterAppointmentOptionTable, database.MasterAppointmentTable)
	err := r.db.Get(&exists, queryGetOption, optionId)
	if err != nil {
		return false, fmt.Errorf("failed to check active option with id = %d for all users: %w", optionId, err)
	}

	return exists, nil
}

func (r *OptionPostgres) CheckingActiveOptionExistenceForMaster(optionId, masterId int) (bool, error) {
	var exists bool

	queryGetOption := fmt.Sprintf(`SELECT EXISTS(SELECT 1 FROM %s AS app_opt
		INNER JOIN %s AS app ON app.id = app_opt.master_appointment_id 
		WHERE app_opt.option_id = $1 AND app.users_id = $2 AND (
                app.date > CURRENT_DATE OR 
                (app.date = CURRENT_DATE AND app.start_time::time >= CURRENT_TIME)
            ))`, database.MasterAppointmentOptionTable, database.MasterAppointmentTable)
	err := r.db.Get(&exists, queryGetOption, optionId, masterId)
	if err != nil {
		return false, fmt.Errorf("failed to check active option with id = %d for master with id = %d: %w", optionId, masterId, err)
	}

	return exists, nil
}
