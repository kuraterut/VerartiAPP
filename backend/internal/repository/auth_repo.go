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

type AuthPostgres struct {
	db *sqlx.DB
}

func NewAuthPostgres(db *sqlx.DB) *AuthPostgres {
	return &AuthPostgres{db: db}
}

func (r *AuthPostgres) CreateUser(user models.Users, roleId int) (int, error) {
	var id int

	query := fmt.Sprintf("INSERT INTO %s (name, surname, patronymic, password_hash, email, phone, role_id)"+
		" VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING id", database.UserTable)
	row := r.db.QueryRow(query, user.Name, user.Surname, user.Patronymic,
		user.Password, user.Email, user.Phone, roleId)
	if err := row.Scan(&id); err != nil {
		return 0, err
	}

	return id, nil
}

func (r *AuthPostgres) GetUser(phone, password string) (models.Users, error) {
	var user models.Users
	query := fmt.Sprintf("SELECT us.id as id, rl.name as role"+
		" FROM %s us INNER JOIN %s rl on us.role_id = rl.id"+
		" WHERE phone = $1 AND password_hash = $2", database.UserTable, database.RoleTable)
	err := r.db.Get(&user, query, phone, password)
	if errors.Is(err, sql.ErrNoRows) {
		return models.Users{}, internal.NewErrorResponse(401, "incorrect login or password")
	}

	return user, err
}
