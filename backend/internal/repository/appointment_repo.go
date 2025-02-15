package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"strings"
	"verarti/internal"
	"verarti/models"
	"verarti/pkg/database"
)

type AppointmentPostgres struct {
	db *sqlx.DB
}

func NewAppointmentPostgres(db *sqlx.DB) *AppointmentPostgres {
	return &AppointmentPostgres{db: db}
}

func (r *AppointmentPostgres) CreateAppointment(appointment models.Appointment) (int, error) {
	var id int
	query := fmt.Sprintf("INSERT INTO %s (name, description, duration, price)"+
		"VALUES ($1, $2, $3, $4) RETURNING id", database.AppointmentTable)
	row := r.db.QueryRow(query, appointment.Name, appointment.Description, appointment.Duration, appointment.Price)
	if err := row.Scan(&id); err != nil {
		return 0, err
	}

	return id, nil
}

func (r *AppointmentPostgres) GetAllAppointments() ([]models.Appointment, error) {
	var appointments []models.Appointment
	query := fmt.Sprintf("SELECT * FROM %s", database.AppointmentTable)
	err := r.db.Select(&appointments, query)

	return appointments, err
}

func (r *AppointmentPostgres) GetAppointmentById(appointmentId int) (models.Appointment, error) {
	var appointment models.Appointment
	query := fmt.Sprintf("SELECT * FROM %s WHERE id = $1", database.AppointmentTable)
	err := r.db.Get(&appointment, query, appointmentId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return models.Appointment{}, internal.NewErrorResponse(404, "appointment not found")
		}

		return models.Appointment{}, err
	}

	return appointment, err
}

func (r *AppointmentPostgres) UpdateAppointment(appointment models.AppointmentUpdate, appointmentId int) error {
	var id int
	queryGetAppointment := fmt.Sprintf("SELECT id FROM %s WHERE id = $1", database.AppointmentTable)
	err := r.db.Get(&id, queryGetAppointment, appointmentId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return internal.NewErrorResponse(404, "appointment with this id was not found")
		}

		return err
	}

	setValues := make([]string, 0)
	args := make([]interface{}, 0)
	argId := 1

	if appointment.Name != "" {
		setValues = append(setValues, fmt.Sprintf("name=$%d", argId))
		args = append(args, appointment.Name)
		argId++
	}

	if appointment.Description != "" {
		setValues = append(setValues, fmt.Sprintf("description=$%d", argId))
		args = append(args, appointment.Description)
		argId++
	}

	if appointment.Duration != "" {
		setValues = append(setValues, fmt.Sprintf("duration=$%d", argId))
		args = append(args, appointment.Duration)
		argId++
	}

	if appointment.Price != 0 {
		setValues = append(setValues, fmt.Sprintf("price=$%d", argId))
		args = append(args, appointment.Price)
		argId++
	}

	setQuery := strings.Join(setValues, ", ")

	if setQuery == "" {
		return internal.NewErrorResponse(400, "invalid input body: it is empty")
	}

	query := fmt.Sprintf("UPDATE %s SET %s WHERE id = $%d",
		database.AppointmentTable, setQuery, argId)
	args = append(args, appointmentId)

	_, err = r.db.Exec(query, args...)
	return err
}

func (r *AppointmentPostgres) DeleteAppointment(appointmentId int) error {
	var id int
	queryGetAppointment := fmt.Sprintf("SELECT id FROM %s WHERE id = $1", database.AppointmentTable)
	err := r.db.Get(&id, queryGetAppointment, appointmentId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return internal.NewErrorResponse(404, "appointment with this id was not found")
		}

		return err
	}

	query := fmt.Sprintf(`DELETE FROM %s WHERE id = $1`, database.AppointmentTable)
	_, err = r.db.Exec(query, appointmentId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return internal.NewErrorResponse(404, "appointment not found")
		}

		return err
	}

	return nil
}

func (r *AppointmentPostgres) AddAppointmentForMaster(masterId, appointmentId int) (int, error) {
	var id int

	queryGetUser := fmt.Sprintf(`
		SELECT us.id FROM %s us
		WHERE us.id = $1 AND EXISTS (
			SELECT 1
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id AND rl.name = 'master'
		)`, database.UserTable, database.UsersRoleTable, database.RoleTable)
	err := r.db.Get(&id, queryGetUser, masterId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return 0, internal.NewErrorResponse(404, "master with this id was not found")
		}

		return 0, err
	}

	queryGetAppointment := fmt.Sprintf("SELECT id FROM %s WHERE id = $1", database.AppointmentTable)
	err = r.db.Get(&id, queryGetAppointment, appointmentId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return 0, internal.NewErrorResponse(404, "appointment with this id was not found")
		}

		return 0, err
	}

	queryAddAppointment := fmt.Sprintf("INSERT INTO %s (users_id, appointment_id)"+
		"VALUES ($1, $2) RETURNING id", database.UsersAppointmentTable)
	row := r.db.QueryRow(queryAddAppointment, masterId, appointmentId)
	if err := row.Scan(&id); err != nil {
		return 0, err
	}

	return id, nil
}
