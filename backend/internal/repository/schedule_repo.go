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

func (r *SchedulePostgres) GetAllMastersByDate(date string, isAppointed bool) ([]models.Users, error) {
	var masters []models.Users

	queryGetMasters := ""
	if isAppointed {
		queryGetMasters = fmt.Sprintf(`
		SELECT us.id, us.name, us.surname, us.patronymic, us.email, us.phone, us.bio, us.photo, us.current_salary,
	   (
	   SELECT array_remove(array_agg(rl.name), NULL)
	   FROM %s AS us_rl
	   LEFT JOIN %s AS rl ON us_rl.role_id = rl.id
	   WHERE us_rl.users_id = us.id
	   ) AS roles
		FROM %s ms_sh
		INNER JOIN %s AS us ON us.id = ms_sh.users_id
		WHERE ms_sh.date = $1`, database.UsersRoleTable, database.RoleTable, database.MasterShiftTable, database.UserTable)
	} else {
		queryGetMasters = fmt.Sprintf(`
		SELECT us.id, us.name, us.surname, us.patronymic, us.email, us.phone, us.bio, us.photo, us.current_salary,
	   (
	   SELECT array_remove(array_agg(rl.name), NULL)
	   FROM %s AS us_rl
	   LEFT JOIN %s AS rl ON us_rl.role_id = rl.id
	   WHERE us_rl.users_id = us.id
	   ) AS roles
		FROM %s us
			WHERE EXISTS (
			SELECT 1 FROM %s AS us_rl
			LEFT JOIN %s AS rl ON us_rl.role_id = rl.id
			WHERE us_rl.users_id = us.id AND rl.name = 'master'
		) AND us.id NOT IN (
			SELECT users_id FROM %s WHERE date = $1
		    )`, database.UsersRoleTable, database.RoleTable, database.UserTable, database.UsersRoleTable, database.RoleTable, database.MasterShiftTable)
	}

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
		if isAppointed {
			return nil, internal.NewErrorResponse(404, "there are no appointed masters for this date")
		}

		return nil, internal.NewErrorResponse(404, "no free masters. Everything is booked for this date")
	}

	return masters, nil
}

func (r *SchedulePostgres) CreateSchedule(schedule models.MasterScheduleInput) (int, error) {
	var id int

	queryGetMaster := fmt.Sprintf(`
		SELECT us.id FROM %s us
		WHERE us.id = $1 AND EXISTS (
			SELECT 1
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id AND rl.name = 'master'
		)`, database.UserTable, database.UsersRoleTable, database.RoleTable)
	err := r.db.Get(&id, queryGetMaster, schedule.MasterId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return 0, internal.NewErrorResponse(404, "master with this id was not found")
		}

		return 0, err
	}

	queryGetClient := fmt.Sprintf(`SELECT id FROM %s WHERE id = $1`, database.ClientTable)
	err = r.db.Get(&id, queryGetClient, schedule.ClientId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return 0, internal.NewErrorResponse(404, "client with this id was not found")
		}

		return 0, err
	}

	tx, err := r.db.Begin()
	if err != nil {
		return 0, err
	}
	defer tx.Rollback()

	var scheduleId int
	query := fmt.Sprintf("INSERT INTO %s (users_id, client_id, start_time, date)"+
		"VALUES ($1, $2, $3, $4) RETURNING id", database.MasterScheduleTable)
	row := tx.QueryRow(query, schedule.MasterId, schedule.ClientId, schedule.StartTime, schedule.Date)
	if err := row.Scan(&scheduleId); err != nil {
		return 0, err
	}

	for _, optionId := range schedule.OptionIds {
		queryGetOption := fmt.Sprintf(`SELECT id FROM %s WHERE id = $1`, database.OptionTable)
		err = r.db.Get(&id, queryGetOption, optionId)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return 0, internal.NewErrorResponse(404, fmt.Sprintf("option with this id: %d was not found", optionId))
			}

			return 0, err
		}

		queryGetOptionAndMaster := fmt.Sprintf(`SELECT id FROM %s WHERE users_id = $1 AND option_id = $2`, database.UsersOptionTable)
		err = r.db.Get(&id, queryGetOptionAndMaster, schedule.MasterId, optionId)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return 0, internal.NewErrorResponse(404, fmt.Sprintf("the master does not provide a option with this id: %d", optionId))
			}

			return 0, err
		}

		query := fmt.Sprintf("INSERT INTO %s (option_id, master_schedule_id)"+
			"VALUES ($1, $2) RETURNING id", database.MasterScheduleOptionTable)
		row := tx.QueryRow(query, optionId, scheduleId)
		if err := row.Scan(&id); err != nil {
			return 0, err
		}
	}

	return scheduleId, tx.Commit()
}

func (r *SchedulePostgres) GetScheduleByClientId(clientId int) ([]models.MasterSchedule, error) {
	//var (
	//	id        int
	//	schedules []models.MasterSchedule
	//	schedule  models.MasterScheduleInput
	//	master    models.Users
	//	client    models.Client
	//)
	//
	//// проверка существования клиента с таким id
	//queryGetClient := fmt.Sprintf(`SELECT id FROM %s WHERE id = $1`, database.ClientTable)
	//err := r.db.Get(&id, queryGetClient, clientId)
	//if err != nil {
	//	if errors.Is(err, sql.ErrNoRows) {
	//		return nil, internal.NewErrorResponse(404, "client with this id was not found")
	//	}
	//
	//	return nil, err
	//}
	//
	//query := fmt.Sprintf(`
	//	SELECT sch.id AS id, TO_CHAR(sch.start_time, 'HH:MM') AS start_time, TO_CHAR(sch.date, 'YYYY-MM-DD') AS date, st.name AS status
	//   	(
	//		SELECT array_remove(array_agg(rl.name), NULL)
	//		FROM %s AS us_rl
	//		LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
	//		WHERE us_rl.users_id = us.id
	//	) AS status
	//	FROM %s AS sch
	//	INNER JOIN %s AS st on st.id = sch.status_id
	//	WHERE us.id = $1`, database.UsersRoleTable, database.RoleTable, database.UserTable)
	//rows, err := r.db.Query(query, clientId)
	//if err != nil {
	//	if errors.Is(err, sql.ErrNoRows) {
	//		return nil, internal.NewErrorResponse(404, "no schedules found for this client")
	//	}
	//
	//	return nil, err
	//}
	//defer rows.Close()
	//
	//for rows.Next() {
	//	err = rows.Scan(&user.Id, &user.Name, &user.Surname, &user.Patronymic, &user.Email, &user.Phone, &user.Bio, &user.Photo, &user.CurSalary, pq.Array(&user.Roles))
	//	if err != nil {
	//		return nil, err
	//	}
	//
	//	if user.Roles == nil {
	//		continue
	//	}
	//
	//	admins = append(admins, user)
	//}
	//
	//if user.Roles == nil {
	//	return models.Users{}, internal.NewErrorResponse(500, "the user does not have any roles")
	//}

	return nil, nil
}
