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

type ProfilePostgres struct {
	db *sqlx.DB
}

func NewProfilePostgres(db *sqlx.DB) *ProfilePostgres {
	return &ProfilePostgres{db: db}
}

func (r *ProfilePostgres) GetUserInfo(userId int) (models.Users, error) {
	var user models.Users
	query := fmt.Sprintf(`
		SELECT us.id, us.name, us.surname, us.patronymic, us.email, us.phone, us.bio, us.photo, us.current_salary, 
	   	(
			SELECT array_remove(array_agg(rl.name), NULL)
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id
		) AS roles
		FROM %s us
		WHERE us.id = $1`, database.UsersRoleTable, database.RoleTable, database.UserTable)

	row := r.db.QueryRow(query, userId)
	err := row.Scan(&user.Id, &user.Name, &user.Surname, &user.Patronymic, &user.Email, &user.Phone, &user.Bio, &user.Photo, &user.CurSalary, pq.Array(&user.Roles))
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return models.Users{}, internal.NewErrorResponse(404, "user not found")
		}

		return models.Users{}, err
	}

	if user.Roles == nil {
		return models.Users{}, internal.NewErrorResponse(500, "the user does not have any roles")
	}

	return user, err
}

func (r *ProfilePostgres) UpdatePhoto(userId int, newPhoto []byte) error {
	return nil
}
