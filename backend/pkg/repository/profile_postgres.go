package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"verarti/models"
	"verarti/pkg"
)

type ProfilePostgres struct {
	db *sqlx.DB
}

func NewProfilePostgres(db *sqlx.DB) *ProfilePostgres {
	return &ProfilePostgres{db: db}
}

func (r *ProfilePostgres) GetUserInfo(userId int) (models.Users, error) {
	var user models.Users
	query := fmt.Sprintf("Select * from %s WHERE id = $1", userTable)
	err := r.db.Get(&user, query, userId)
	if errors.Is(err, sql.ErrNoRows) {
		return models.Users{}, pkg.NewErrorResponse(404, "user not found")
	}

	return user, err
}
