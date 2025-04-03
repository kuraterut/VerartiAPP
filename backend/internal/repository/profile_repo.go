package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
	"strings"
	"verarti/internal/domain"
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
			return models.Users{}, domain.NewErrorResponse(404, "user not found")
		}

		return models.Users{}, err
	}

	if user.Roles == nil {
		return models.Users{}, domain.NewErrorResponse(500, "the user does not have any roles")
	}

	return user, err
}

func (r *ProfilePostgres) UpdatePhoto(userId int, newPhoto []byte) error {
	return nil
}

func (r *ProfilePostgres) UpdateInfo(userId int, info models.UpdateInfo) error {
	setValues := make([]string, 0)
	args := make([]interface{}, 0)
	argId := 1

	if info.Name != nil {
		setValues = append(setValues, fmt.Sprintf("name=$%d", argId))
		args = append(args, *info.Name)
		argId++
	}

	if info.Surname != nil {
		setValues = append(setValues, fmt.Sprintf("surname=$%d", argId))
		args = append(args, *info.Surname)
		argId++
	}

	if info.Patronymic != nil {
		setValues = append(setValues, fmt.Sprintf("patronymic=$%d", argId))
		args = append(args, *info.Patronymic)
		argId++
	}

	if info.Bio != nil {
		setValues = append(setValues, fmt.Sprintf("bio=$%d", argId))
		args = append(args, *info.Bio)
		argId++
	}

	if len(setValues) == 0 {
		return nil
	}

	setQuery := strings.Join(setValues, ", ")

	query := fmt.Sprintf("UPDATE %s SET %s WHERE id = $%d",
		database.UserTable, setQuery, argId)
	args = append(args, userId)

	_, err := r.db.Exec(query, args...)
	return err
}

func (r *ProfilePostgres) UpdatePassword(userId int, newPasswordHash, oldPasswordHash string) error {
	var exists bool
	queryGetUser := fmt.Sprintf("SELECT EXISTS(SELECT 1 FROM %s WHERE id = $1 AND password_hash = $2)", database.UserTable)
	err := r.db.Get(&exists, queryGetUser, userId, oldPasswordHash)
	if err != nil {
		return err
	}

	if !exists {
		return domain.NewErrorResponse(403, "incorrect old password")
	}

	query := fmt.Sprintf("UPDATE %s SET password_hash = $1 WHERE id = $2",
		database.UserTable)
	_, err = r.db.Exec(query, newPasswordHash, userId)
	return err
}
