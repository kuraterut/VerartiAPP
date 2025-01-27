package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
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
	if errors.Is(err, sql.ErrNoRows) {
		return models.Appointment{}, internal.NewErrorResponse(404, "appointment not found")
	}

	return appointment, err
}
