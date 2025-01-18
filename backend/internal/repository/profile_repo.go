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

type ProfilePostgres struct {
	db *sqlx.DB
}

func NewProfilePostgres(db *sqlx.DB) *ProfilePostgres {
	return &ProfilePostgres{db: db}
}

func (r *ProfilePostgres) GetUserInfo(userId int) (models.Users, error) {
	var user models.Users
	queryGetUser := fmt.Sprintf("SELECT id, name, surname, patronymic, email, phone, bio, photo, salary FROM %s WHERE id = $1", database.UserTable)
	err := r.db.Get(&user, queryGetUser, userId)
	if errors.Is(err, sql.ErrNoRows) {
		return models.Users{}, internal.NewErrorResponse(404, "user not found")
	}

	queryGetRoles := fmt.Sprintf("SELECT rl.name as roles FROM %s us_rl INNER JOIN %s rl on rl.id = us_rl.role_id "+
		" WHERE us_rl.users_id = $1", database.UsersRoleTable, database.RoleTable)
	err = r.db.Select(&user.Roles, queryGetRoles, user.Id)
	if errors.Is(err, sql.ErrNoRows) {
		return models.Users{}, internal.NewErrorResponse(500, "the user does not have any roles")
	}

	return user, err
}

func (r *ProfilePostgres) UpdatePhoto(userId int, newPhoto []byte) error {
	return nil
}
