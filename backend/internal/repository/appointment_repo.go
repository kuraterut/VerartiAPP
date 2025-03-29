package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"verarti/internal/domain"
	"verarti/models"
	"verarti/pkg/database"
)

type AppointmentPostgres struct {
	userPostgres   *UserPostgres
	clientPostgres *ClientPostgres
	db             *sqlx.DB
}

func NewAppointmentPostgres(db *sqlx.DB, userPostgres *UserPostgres, clientPostgres *ClientPostgres) *AppointmentPostgres {
	return &AppointmentPostgres{db: db, userPostgres: userPostgres, clientPostgres: clientPostgres}
}

func (r *AppointmentPostgres) PutAdminToDate(adminShift models.AdminShift) error {
	var id int

	queryGetAdmin := fmt.Sprintf(`
		SELECT us.id FROM %s us
		WHERE us.id = $1 AND EXISTS (
			SELECT 1
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id AND rl.name = '%s'
		)`, database.UserTable, database.UsersRoleTable, database.RoleTable, domain.AdminRole)
	err := r.db.Get(&id, queryGetAdmin, adminShift.AdminId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return domain.NewErrorResponse(404, "admin with this id was not found")
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

func (r *AppointmentPostgres) PutMasterToDate(masterShift models.MasterShift) error {
	var id int

	queryGetMaster := fmt.Sprintf(`
		SELECT us.id FROM %s us
		WHERE us.id = $1 AND EXISTS (
			SELECT 1
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id AND rl.name = '%s'
		)`, database.UserTable, database.UsersRoleTable, database.RoleTable, domain.MasterRole)
	err := r.db.Get(&id, queryGetMaster, masterShift.MasterId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return domain.NewErrorResponse(404, "master with this id was not found")
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

func (r *AppointmentPostgres) GetAdminByDate(date string) (models.Users, error) {
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
		return models.Users{}, domain.NewErrorResponse(404, "there is no appointed admin for this date")
	}

	return admins[0], nil
}

func (r *AppointmentPostgres) GetAllMastersByDate(date string, isAppointed bool) ([]models.Users, error) {
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
			WHERE us_rl.users_id = us.id AND rl.name = '%s'
		) AND us.id NOT IN (
			SELECT users_id FROM %s WHERE date = $1
		    )`, database.UsersRoleTable, database.RoleTable, database.UserTable, database.UsersRoleTable, database.RoleTable, domain.MasterRole, database.MasterShiftTable)
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
		return []models.Users{}, nil
	}

	return masters, nil
}

func (r *AppointmentPostgres) CreateAppointment(appointment models.MasterAppointmentInput) (int, error) {
	var id int

	queryGetMaster := fmt.Sprintf(`
		SELECT us.id FROM %s us
		WHERE us.id = $1 AND EXISTS (
			SELECT 1
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id AND rl.name = '%s'
		)`, database.UserTable, database.UsersRoleTable, database.RoleTable, domain.MasterRole)
	err := r.db.Get(&id, queryGetMaster, appointment.MasterId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return 0, domain.NewErrorResponse(404, "master with this id was not found")
		}

		return 0, err
	}

	queryGetClient := fmt.Sprintf(`SELECT id FROM %s WHERE id = $1`, database.ClientTable)
	err = r.db.Get(&id, queryGetClient, appointment.ClientId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return 0, domain.NewErrorResponse(404, "client with this id was not found")
		}

		return 0, err
	}

	tx, err := r.db.Begin()
	if err != nil {
		return 0, err
	}
	defer tx.Rollback()

	var appointmentId int
	query := fmt.Sprintf("INSERT INTO %s (users_id, client_id, start_time, date, comment)"+
		"VALUES ($1, $2, $3, $4, $5) RETURNING id", database.MasterAppointmentTable)
	row := tx.QueryRow(query, appointment.MasterId, appointment.ClientId, appointment.StartTime, appointment.Date, appointment.Comment)
	if err := row.Scan(&appointmentId); err != nil {
		return 0, err
	}

	for _, optionId := range appointment.OptionIds {
		queryGetOption := fmt.Sprintf(`SELECT id FROM %s WHERE id = $1`, database.OptionTable)
		err = r.db.Get(&id, queryGetOption, optionId)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return 0, domain.NewErrorResponse(404, fmt.Sprintf("option with this id: %d was not found", optionId))
			}

			return 0, err
		}

		queryGetOptionAndMaster := fmt.Sprintf(`SELECT id FROM %s WHERE users_id = $1 AND option_id = $2`, database.UsersOptionTable)
		err = r.db.Get(&id, queryGetOptionAndMaster, appointment.MasterId, optionId)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return 0, domain.NewErrorResponse(404, fmt.Sprintf("the master does not provide a option with this id: %d", optionId))
			}

			return 0, err
		}

		query := fmt.Sprintf("INSERT INTO %s (option_id, master_appointment_id)"+
			"VALUES ($1, $2) RETURNING id", database.MasterAppointmentOptionTable)
		row := tx.QueryRow(query, optionId, appointmentId)
		if err := row.Scan(&id); err != nil {
			return 0, err
		}
	}

	return appointmentId, tx.Commit()
}

func (r *AppointmentPostgres) GetAppointmentByClientId(clientId int) ([]models.MasterAppointment, error) {
	var (
		appointments []models.MasterAppointment
	)

	client, err := r.clientPostgres.GetClientById(clientId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, domain.NewErrorResponse(404, fmt.Sprintf("client with this id = %d not found", clientId))
		}

		return nil, err
	}

	query := fmt.Sprintf(`
		SELECT app.id AS id, app.start_time AS start_time, TO_CHAR(app.date, 'YYYY-MM-DD') AS date, 
		st.name AS status, app.comment AS comment, app.users_id, app.client_id
		FROM %s AS app
		INNER JOIN %s AS st on st.id = app.status_id
		WHERE app.client_id = $1`, database.MasterAppointmentTable, database.StatusTable)
	rows, err := r.db.Query(query, clientId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, nil
		}

		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var (
			masterId, clientId int
			appointment        models.MasterAppointment
			master             models.Users
			options            []models.Option
		)

		err = rows.Scan(&appointment.Id, &appointment.StartTime, &appointment.Date, &appointment.Status, &appointment.Comment, &masterId, &clientId)
		if err != nil {
			return nil, err
		}

		master, err = r.userPostgres.GetMasterById(masterId)
		if err != nil {
			continue // todo возможно стоит по-другому обрабатывать эти ошибки. Например логировать их в какой-то файл
		}

		queryGetOptions := fmt.Sprintf("SELECT opt.id, opt.name, opt.description, opt.duration, opt.price "+
			" FROM %s as opt "+
			" INNER JOIN %s as app_opt on app_opt.option_id = opt.id"+
			" WHERE app_opt.master_appointment_id = $1", database.OptionTable, database.MasterAppointmentOptionTable)
		err = r.db.Select(&options, queryGetOptions, appointment.Id)
		if err != nil {
			continue
		}

		appointment.Client = client
		appointment.Master = master
		appointment.Options = options

		appointments = append(appointments, appointment)
	}

	if len(appointments) == 0 {
		return []models.MasterAppointment{}, nil
	}

	return appointments, nil
}

func (r *AppointmentPostgres) GetAllAppointmentsByDate(date string) ([]models.MasterAppointment, error) {
	var (
		appointments []models.MasterAppointment
	)

	query := fmt.Sprintf(`
		SELECT app.id AS id, app.start_time AS start_time, TO_CHAR(app.date, 'YYYY-MM-DD') AS date, 
		st.name AS status, app.comment AS comment, app.users_id, app.client_id
		FROM %s AS app
		INNER JOIN %s AS st on st.id = app.status_id
		WHERE app.date = $1`, database.MasterAppointmentTable, database.StatusTable)
	rows, err := r.db.Query(query, date)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, nil
		}

		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var (
			masterId, clientId int
			appointment        models.MasterAppointment
			master             models.Users
			client             models.Client
			options            []models.Option
		)

		err = rows.Scan(&appointment.Id, &appointment.StartTime, &appointment.Date, &appointment.Status, &appointment.Comment, &masterId, &clientId)
		if err != nil {
			return nil, err
		}

		master, err = r.userPostgres.GetMasterById(masterId)
		if err != nil {
			continue // todo возможно стоит по-другому обрабатывать эти ошибки. Например логировать их в какой-то файл
		}

		client, err = r.clientPostgres.GetClientById(clientId)
		if err != nil {
			continue
		}

		queryGetOptions := fmt.Sprintf("SELECT opt.id, opt.name, opt.description, opt.duration, opt.price "+
			" FROM %s as opt "+
			" INNER JOIN %s as app_opt on app_opt.option_id = opt.id"+
			" WHERE app_opt.master_appointment_id = $1", database.OptionTable, database.MasterAppointmentOptionTable)
		err = r.db.Select(&options, queryGetOptions, appointment.Id)
		if err != nil {
			continue
		}

		appointment.Client = client
		appointment.Master = master
		appointment.Options = options

		appointments = append(appointments, appointment)
	}

	if len(appointments) == 0 {
		return []models.MasterAppointment{}, nil
	}

	return appointments, nil
}

func (r *AppointmentPostgres) GetAppointmentById(appointmentId int) (models.MasterAppointment, error) {
	var (
		masterId, clientId int
		master             models.Users
		client             models.Client
		options            []models.Option
		appointment        models.MasterAppointment
		appointments       []models.MasterAppointment
	)

	query := fmt.Sprintf(`
		SELECT app.id AS id, app.start_time AS start_time, TO_CHAR(app.date, 'YYYY-MM-DD') AS date, 
		st.name AS status, app.comment AS comment, app.users_id, app.client_id
		FROM %s AS app
		INNER JOIN %s AS st on st.id = app.status_id
		WHERE app.id = $1`, database.MasterAppointmentTable, database.StatusTable)
	rows, err := r.db.Query(query, appointmentId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return models.MasterAppointment{}, domain.NewErrorResponse(404, "appointment with this id not found")
		}

		return models.MasterAppointment{}, err
	}
	defer rows.Close()

	for rows.Next() {
		err = rows.Scan(&appointment.Id, &appointment.StartTime, &appointment.Date, &appointment.Status, &appointment.Comment, &masterId, &clientId)
		if err != nil {
			return models.MasterAppointment{}, err
		}

		master, err = r.userPostgres.GetMasterById(masterId)
		if err != nil {
			return models.MasterAppointment{}, domain.NewErrorResponse(404, fmt.Sprintf("master id = %d, error: %v", masterId, err))
		}

		client, err = r.clientPostgres.GetClientById(clientId)
		if err != nil {
			return models.MasterAppointment{}, domain.NewErrorResponse(404, fmt.Sprintf("client id = %d, error: %v", clientId, err))
		}

		queryGetOptions := fmt.Sprintf("SELECT opt.id, opt.name, opt.description, opt.duration, opt.price "+
			" FROM %s as opt "+
			" INNER JOIN %s as app_opt on app_opt.option_id = opt.id"+
			" WHERE app_opt.master_appointment_id = $1", database.OptionTable, database.MasterAppointmentOptionTable)
		err = r.db.Select(&options, queryGetOptions, appointment.Id)
		if err != nil {
			return models.MasterAppointment{}, domain.NewErrorResponse(500, fmt.Sprintf("error with getting options: %v", err))
		}

		appointment.Client = client
		appointment.Master = master
		appointment.Options = options

		appointments = append(appointments, appointment)
	}

	if len(appointments) == 0 {
		return models.MasterAppointment{}, domain.NewErrorResponse(404, "appointment with this id not found")
	}

	if len(appointments) > 1 {
		return models.MasterAppointment{}, domain.NewErrorResponse(500, "multiple records found for this id")
	}

	return appointment, err
}
