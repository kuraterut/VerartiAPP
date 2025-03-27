package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"verarti/internal/domain"
	"verarti/models"
	"verarti/pkg/database"
)

type AuthPostgres struct {
	db *sqlx.DB
}

func NewAuthPostgres(db *sqlx.DB) *AuthPostgres {
	return &AuthPostgres{db: db}
}

func (r *AuthPostgres) CreateUser(user models.Users, roleIds []int) (int, error) {
	if roleIds == nil {
		return 0, errors.New("the user does not have any roles")
	}

	tx, err := r.db.Begin()
	if err != nil {
		return 0, err
	}
	defer tx.Rollback()

	var userId, id int

	queryCreateUser := fmt.Sprintf("INSERT INTO %s (name, surname, patronymic, password_hash, email, phone)"+
		" VALUES ($1, $2, $3, $4, $5, $6) RETURNING id", database.UserTable)
	row := tx.QueryRow(queryCreateUser, user.Name, user.Surname, user.Patronymic,
		user.Password, user.Email, user.Phone)
	if err := row.Scan(&userId); err != nil {
		return 0, err
	}

	for _, roleId := range roleIds {
		queryCreateUserRole := fmt.Sprintf("INSERT INTO %s (users_id, role_id)"+
			" VALUES ($1, $2) RETURNING id", database.UsersRoleTable)
		row = tx.QueryRow(queryCreateUserRole, userId, roleId)
		if err := row.Scan(&id); err != nil {
			return 0, err
		}
	}

	return userId, tx.Commit()
}

func (r *AuthPostgres) GetUser(phone, password string) (models.Users, error) {
	var user models.Users
	query := fmt.Sprintf(`
		SELECT us.id, array_remove(array_agg(rl.name), NULL) as roles
		FROM %s us
		LEFT JOIN %s us_rl ON us.id = us_rl.users_id
		LEFT JOIN %s rl ON rl.id = us_rl.role_id
		WHERE us.phone = $1 AND us.password_hash = $2
		GROUP BY us.id`, database.UserTable, database.UsersRoleTable, database.RoleTable)

	row := r.db.QueryRow(query, phone, password)
	err := row.Scan(&user.Id, pq.Array(&user.Roles))
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return models.Users{}, domain.NewErrorResponse(401, "incorrect login or password")
		}

		return models.Users{}, err
	}

	if user.Roles == nil {
		return models.Users{}, domain.NewErrorResponse(500, "the user does not have any roles")
	}

	return user, nil
}
