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

	query := fmt.Sprintf("INSERT INTO %s (users_id, day)"+
		"VALUES ($1, $2) RETURNING id", database.AdminShiftTable)
	row := r.db.QueryRow(query, adminShift.AdminId, adminShift.Day)
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

	query := fmt.Sprintf("INSERT INTO %s (users_id, day)"+
		"VALUES ($1, $2) RETURNING id", database.MasterShiftTable)
	row := r.db.QueryRow(query, masterShift.MasterId, masterShift.Day)
	if err := row.Scan(&id); err != nil {
		return err
	}

	return nil
}
