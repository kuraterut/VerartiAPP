package repository

import (
	"fmt"
	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"verarti/internal"
	"verarti/models"
	"verarti/pkg/database"
)

type UserPostgres struct {
	db *sqlx.DB
}

func NewUserPostgres(db *sqlx.DB) *UserPostgres {
	return &UserPostgres{db: db}
}

func (r *UserPostgres) GetAllMasters() ([]models.Users, error) {
	var masters []models.Users

	query := fmt.Sprintf(`
		SELECT us.id, us.name, us.surname, us.patronymic, us.email, us.phone, us.bio, us.photo, us.current_salary, 
	   	(
			SELECT array_remove(array_agg(rl.name), NULL)
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id
		) AS roles
		FROM %s us
		WHERE EXISTS (
			SELECT 1
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id AND rl.name = 'master'
		)`, database.UsersRoleTable, database.RoleTable, database.UserTable, database.UsersRoleTable, database.RoleTable)

	rows, err := r.db.Query(query)
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
		return nil, internal.NewErrorResponse(404, "masters not found")
	}

	return masters, nil
}
