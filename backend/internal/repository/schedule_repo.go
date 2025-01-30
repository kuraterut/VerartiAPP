package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"verarti/internal"
	"verarti/models"
	"verarti/pkg/database"
)

type SchedulePostgres struct {
	db *sqlx.DB
}

func NewSchedulePostgres(db *sqlx.DB) *SchedulePostgres {
	return &SchedulePostgres{db: db}
}

func (r *SchedulePostgres) PutAdminToDate(adminShift models.AdminShift) error {
	var id int

	queryGetAdmin := fmt.Sprintf(`
		SELECT us.id FROM %s us
		WHERE us.id = $1 AND EXISTS (
			SELECT 1
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id AND rl.name = 'admin'
		)`, database.UserTable, database.UsersRoleTable, database.RoleTable)
	err := r.db.Get(&id, queryGetAdmin, adminShift.AdminId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return internal.NewErrorResponse(404, "admin with this id was not found")
		}

		return err
	}

	query := fmt.Sprintf("INSERT INTO %s (users_id, date)"+
		"VALUES ($1, $2) RETURNING id", database.AdminShiftTable)
	row := r.db.QueryRow(query, adminShift.AdminId, adminShift.Date)
	if err := row.Scan(&id); err != nil {
		return err
	}

	return nil
}

func (r *SchedulePostgres) PutMasterToDate(masterShift models.MasterShift) error {
	var id int

	queryGetMaster := fmt.Sprintf(`
		SELECT us.id FROM %s us
		WHERE us.id = $1 AND EXISTS (
			SELECT 1
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id AND rl.name = 'master'
		)`, database.UserTable, database.UsersRoleTable, database.RoleTable)
	err := r.db.Get(&id, queryGetMaster, masterShift.MasterId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return internal.NewErrorResponse(404, "master with this id was not found")
		}

		return err
	}

	query := fmt.Sprintf("INSERT INTO %s (users_id, date)"+
		"VALUES ($1, $2) RETURNING id", database.MasterShiftTable)
	row := r.db.QueryRow(query, masterShift.MasterId, masterShift.Date)
	if err := row.Scan(&id); err != nil {
		return err
	}

	return nil
}

func (r *SchedulePostgres) GetAdminByDate(date string) (models.Users, error) {
	var admins []models.Users

	queryGetAdmin := fmt.Sprintf(`
		SELECT us.id, us.name, us.surname, us.patronymic, us.email, us.phone, us.bio, us.photo, us.current_salary,
	   (
	   SELECT array_remove(array_agg(rl.name), NULL)
	   FROM %s AS us_rl
	   LEFT JOIN %s AS rl ON us_rl.role_id = rl.id
	   WHERE us_rl.users_id = us.id
	   ) AS roles
		FROM %s ad_sh
		INNER JOIN %s AS us ON us.id = ad_sh.users_id
		WHERE ad_sh.date = $1`, database.UsersRoleTable, database.RoleTable, database.AdminShiftTable, database.UserTable)

	rows, err := r.db.Query(queryGetAdmin, date)
	if err != nil {
		return models.Users{}, err
	}
	defer rows.Close()

	for rows.Next() {
		var user models.Users
		err = rows.Scan(&user.Id, &user.Name, &user.Surname, &user.Patronymic, &user.Email, &user.Phone, &user.Bio, &user.Photo, &user.CurSalary, pq.Array(&user.Roles))
		if err != nil {
			return models.Users{}, err
		}

		if user.Roles == nil {
			continue
		}

		admins = append(admins, user)
	}

	if len(admins) == 0 {
		return models.Users{}, internal.NewErrorResponse(404, "there is no appointed admin for this date")
	}

	return admins[0], nil
}

func (r *SchedulePostgres) GetAllMastersByDate(date string) ([]models.Users, error) {
	var masters []models.Users

	queryGetMasters := fmt.Sprintf(`
		SELECT us.id, us.name, us.surname, us.patronymic, us.email, us.phone, us.bio, us.photo, us.current_salary,
	   (
	   SELECT array_remove(array_agg(rl.name), NULL)
	   FROM %s AS us_rl
	   LEFT JOIN %s AS rl ON us_rl.role_id = rl.id
	   WHERE us_rl.users_id = us.id
	   ) AS roles
		FROM %s ad_sh
		INNER JOIN %s AS us ON us.id = ad_sh.users_id
		WHERE ad_sh.date = $1`, database.UsersRoleTable, database.RoleTable, database.MasterShiftTable, database.UserTable)

	rows, err := r.db.Query(queryGetMasters, date)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var user models.Users
		err = rows.Scan(&user.Id, &user.Name, &user.Surname, &user.Patronymic, &user.Email, &user.Phone, &user.Bio, &user.Photo, &user.CurSalary, pq.Array(&user.Roles))
		if err != nil {
			return nil, err
		}

		if user.Roles == nil {
			continue
		}

		masters = append(masters, user)
	}

	if len(masters) == 0 {
		return nil, internal.NewErrorResponse(404, "there are no appointed masters for this date")
	}

	return masters, nil
}
