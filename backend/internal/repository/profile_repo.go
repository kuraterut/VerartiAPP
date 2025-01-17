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
	query := fmt.Sprintf("SELECT us.id as id, rl.name as role"+
		" FROM %s us INNER JOIN %s rl on us.role_id = rl.id"+
		" WHERE us.id = $1", userTable, roleTable)
	err := r.db.Get(&user, query, userId)
	if errors.Is(err, sql.ErrNoRows) {
		return models.Users{}, pkg.NewErrorResponse(404, "user not found")
	}

	return user, err
}

func (r *ProfilePostgres) UpdatePhoto(userId int, newPhoto []byte) error {

}
