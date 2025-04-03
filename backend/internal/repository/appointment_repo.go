package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"net/http"
	"reflect"
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

	queryGetAdminByDate := fmt.Sprintf("SELECT ad_sh.id FROM %s ad_sh "+
		" WHERE ad_sh.date = $1", database.AdminShiftTable)
	err := r.db.Get(&id, queryGetAdminByDate, adminShift.Date)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		return err
	}

	if errors.Is(err, sql.ErrNoRows) {
		query := fmt.Sprintf("INSERT INTO %s (users_id, date)"+
			"VALUES ($1, $2) RETURNING id", database.AdminShiftTable)
		row := r.db.QueryRow(query, adminShift.AdminId, adminShift.Date)
		if err := row.Scan(&id); err != nil {
			return err
		}

		return nil
	}

	query := fmt.Sprintf("UPDATE %s SET users_id = $1 WHERE id = $2", database.AdminShiftTable)
	_, err = r.db.Exec(query, adminShift.AdminId, id)
	return err
}

func (r *AppointmentPostgres) PutMasterToDate(masterShift models.MasterShift) error {
	var id int

	query := fmt.Sprintf("INSERT INTO %s (users_id, date)"+
		"VALUES ($1, $2) RETURNING id", database.MasterShiftTable)
	row := r.db.QueryRow(query, masterShift.MasterId, masterShift.Date)
	if err := row.Scan(&id); err != nil {
		return err
	}

	return nil
}

func (r *AppointmentPostgres) CancelMasterEntryForDate(masterId int, date string) error {
	var exists bool

	queryCheckAppointmentForMaster := fmt.Sprintf(`
		SELECT EXISTS( SELECT 1 FROM %s WHERE users_id = $1 AND date = $2)`, database.MasterAppointmentTable)
	err := r.db.Get(&exists, queryCheckAppointmentForMaster, masterId, date)
	if err != nil {
		return err
	}

	if exists {
		return domain.NewErrorResponse(409, fmt.Sprintf("shift cannot be cancelled as this master = %d already has appointments", masterId))
	}

	query := fmt.Sprintf("DELETE FROM %s WHERE users_id = $1 AND date = $2", database.MasterShiftTable)
	_, err = r.db.Exec(query, masterId, date)
	return err
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
	var id, exists int

	queryCheckMasterShiftByDate := fmt.Sprintf(`SELECT 1 FROM %s ms_sh WHERE ms_sh.date = $1 AND ms_sh.users_id = $2`, database.MasterShiftTable)
	err := r.db.Get(&exists, queryCheckMasterShiftByDate, appointment.Date, appointment.MasterId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return 0, domain.NewErrorResponse(409, fmt.Sprintf("master with id = %d is not assigned for this date", appointment.MasterId))
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

func (r *AppointmentPostgres) CheckingActiveAppointmentExistenceByMasterId(masterId int) (bool, error) {
	var exists bool

	query := fmt.Sprintf(`
        SELECT EXISTS (
            SELECT 1 FROM %s 
            WHERE users_id = $1 
            AND (
                date > CURRENT_DATE OR 
                (date = CURRENT_DATE AND start_time::time >= CURRENT_TIME)
            )
        )`, database.MasterAppointmentTable)

	err := r.db.Get(&exists, query, masterId)
	if err != nil {
		return false, fmt.Errorf("failed to check active appointments: %w", err)
	}

	return exists, nil
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

func (r *AppointmentPostgres) GetAllAppointmentsByDateAndMasterId(masterId int, date string) ([]models.AppointmentResponseForMaster, error) {
	var (
		appointments []models.AppointmentResponseForMaster
	)

	query := fmt.Sprintf(`
		SELECT app.id AS id, app.start_time AS start_time, TO_CHAR(app.date, 'YYYY-MM-DD') AS date, 
		app.comment AS comment, app.client_id FROM %s AS app 
		WHERE app.date = $1 AND app.users_id = $2`, database.MasterAppointmentTable)
	rows, err := r.db.Query(query, date, masterId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, nil
		}

		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var (
			clientId      int
			appointmentId int
			appointment   models.AppointmentResponseForMaster
			client        models.Client
			optionNames   []string
		)

		err = rows.Scan(&appointmentId, &appointment.StartTime, &appointment.Date, &appointment.Comment, &clientId)
		if err != nil {
			return nil, err
		}

		client, err = r.clientPostgres.GetClientById(clientId)
		if err != nil {
			continue
		}

		queryGetOptions := fmt.Sprintf("SELECT opt.name "+
			" FROM %s as opt "+
			" INNER JOIN %s as app_opt on app_opt.option_id = opt.id"+
			" WHERE app_opt.master_appointment_id = $1", database.OptionTable, database.MasterAppointmentOptionTable)
		err = r.db.Select(&optionNames, queryGetOptions, appointmentId)
		if err != nil {
			continue
		}

		appointment.ClientName = client.Name
		appointment.OptionNames = optionNames

		appointments = append(appointments, appointment)
	}

	if len(appointments) == 0 {
		return []models.AppointmentResponseForMaster{}, nil
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

func (r *AppointmentPostgres) DeleteAppointmentById(appointmentId int) error {
	query := fmt.Sprintf("DELETE FROM %s WHERE id = $1", database.MasterAppointmentTable)
	_, err := r.db.Exec(query, appointmentId)
	return err
}

func (r *AppointmentPostgres) UpdateAppointmentById(appointmentId int, input models.MasterAppointmentUpdate) error {
	var appointmentExists int
	queryCheckAppointment := fmt.Sprintf("SELECT 1 FROM %s WHERE id = $1", database.MasterAppointmentTable)
	err := r.db.Get(&appointmentExists, queryCheckAppointment, appointmentId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return domain.NewErrorResponse(404, fmt.Sprintf("appointment with this id = %d not found", appointmentId))
		}

		return fmt.Errorf("failed to check appointment existence: %w", err)
	}

	tx, err := r.db.Begin()
	if err != nil {
		return err
	}
	defer tx.Rollback()

	if input.Comment != "" {
		queryUpdateComment := fmt.Sprintf("UPDATE %s SET comment = $1 WHERE id = $2", database.MasterAppointmentTable)
		_, err := tx.Exec(queryUpdateComment, input.Comment, appointmentId)
		if err != nil {
			return fmt.Errorf("failed to update comment: %w", err)
		}
	}

	if len(input.OptionIds) == 0 {
		return tx.Commit()
	}

	var oldOptions []int
	query := fmt.Sprintf("SELECT option_id FROM %s WHERE master_appointment_id = $1", database.MasterAppointmentOptionTable)
	err = r.db.Select(&oldOptions, query, appointmentId)
	if err != nil {
		return fmt.Errorf("failed to get old options: %w", err)
	}

	var optionsIdsToInsert, optionsIdsToDelete []int
	for _, oldOptionId := range oldOptions {
		exists := false

		for _, newOptionId := range input.OptionIds {
			if oldOptionId == newOptionId {
				exists = true
				break
			}
		}

		if !exists {
			optionsIdsToDelete = append(optionsIdsToDelete, oldOptionId)
		}
	}
	for _, newOptionId := range input.OptionIds {
		exists := false

		for _, oldOptionId := range oldOptions {
			if oldOptionId == newOptionId {
				exists = true
				break
			}
		}

		if !exists {
			optionsIdsToInsert = append(optionsIdsToInsert, newOptionId)
		}
	}

	for _, optionId := range optionsIdsToInsert {
		var optionExists int
		queryCheckOption := fmt.Sprintf("SELECT 1 FROM %s WHERE id = $1", database.OptionTable)
		err = tx.QueryRow(queryCheckOption, optionId).Scan(&optionExists)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return domain.NewErrorResponse(404, fmt.Sprintf("option with this id = %d not found", optionId))
			}

			return fmt.Errorf("failed to check option existence: %w", err)
		}

		queryUpdateOptions := fmt.Sprintf("INSERT INTO %s (option_id, master_appointment_id) VALUES ($1, $2)", database.MasterAppointmentOptionTable)
		_, err := tx.Exec(queryUpdateOptions, optionId, appointmentId)
		if err != nil {
			return fmt.Errorf("failed to insert option: %w", err)
		}
	}

	for _, optionId := range optionsIdsToDelete {
		queryDeleteOldOptions := fmt.Sprintf("DELETE FROM %s WHERE master_appointment_id = $1 AND option_id = $2", database.MasterAppointmentOptionTable)
		_, err = tx.Exec(queryDeleteOldOptions, appointmentId, optionId)
		if err != nil {
			return fmt.Errorf("failed to delete old option with id = %d: %w", optionId, err)
		}
	}

	return tx.Commit()
}

func (r *AppointmentPostgres) GetMonthlySchedule(schedules []models.DaySchedule) ([]models.DaySchedule, error) {
	var errorResponse *domain.ErrorResponse

	for i, schedule := range schedules {
		admin, err := r.GetAdminByDate(schedule.Date)
		if err != nil && errors.As(err, &errorResponse) {
			if errorResponse.Code != http.StatusNotFound {
				return nil, err
			}
		}

		if isEmptyUserStruct(admin) {
			schedules[i].Admin = nil
		} else {
			schedules[i].Admin = &admin
		}

		masters, err := r.GetAllMastersByDate(schedule.Date, true)
		if err != nil {
			return nil, err
		}

		if masters != nil {
			schedules[i].Masters = masters
		} else {
			schedules[i].Masters = []models.Users{}
		}
	}

	return schedules, nil
}

func isEmptyUserStruct(user models.Users) bool {
	zeroUser := models.Users{}
	return reflect.DeepEqual(user, zeroUser)
}
