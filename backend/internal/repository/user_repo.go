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
			WHERE us_rl.users_id = us.id AND rl.name = '%s'
		)`, database.UsersRoleTable, database.RoleTable, database.UserTable, database.UsersRoleTable, database.RoleTable, domain.MasterRole)

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
		return nil, domain.NewErrorResponse(404, "masters not found")
	}

	return masters, nil
}

func (r *UserPostgres) GetMasterById(masterId int) (models.Users, error) {
	var master models.Users

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

	row := r.db.QueryRow(query, masterId)
	err := row.Scan(&master.Id, &master.Name, &master.Surname, &master.Patronymic, &master.Email, &master.Phone, &master.Bio, &master.Photo, &master.CurSalary, pq.Array(&master.Roles))
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return models.Users{}, domain.NewErrorResponse(404, "user not found")
		}

		return models.Users{}, err
	}

	if master.Roles == nil {
		return models.Users{}, domain.NewErrorResponse(500, "the user does not have any roles")
	} else {
		for _, role := range master.Roles {
			if role == domain.MasterRole {
				return master, nil
			}
		}
	}

	return models.Users{}, domain.NewErrorResponse(400, "this user does not have the master role")
}

func (r *UserPostgres) GetAllAdmins() ([]models.Users, error) {
	var admins []models.Users

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
			WHERE us_rl.users_id = us.id AND rl.name = '%s'
		)`, database.UsersRoleTable, database.RoleTable, database.UserTable, database.UsersRoleTable, database.RoleTable, domain.AdminRole)

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

		admins = append(admins, user)
	}

	if len(admins) == 0 {
		return nil, domain.NewErrorResponse(404, "admins not found")
	}

	return admins, nil
}

func (r *UserPostgres) GetAdminById(masterId int) (models.Users, error) {
	var admin models.Users

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

	row := r.db.QueryRow(query, masterId)
	err := row.Scan(&admin.Id, &admin.Name, &admin.Surname, &admin.Patronymic, &admin.Email, &admin.Phone, &admin.Bio, &admin.Photo, &admin.CurSalary, pq.Array(&admin.Roles))
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return models.Users{}, domain.NewErrorResponse(404, "user not found")
		}

		return models.Users{}, err
	}

	if admin.Roles == nil {
		return models.Users{}, domain.NewErrorResponse(500, "the user does not have any roles")
	} else {
		for _, role := range admin.Roles {
			if role == domain.AdminRole {
				return admin, nil
			}
		}
	}

	return models.Users{}, domain.NewErrorResponse(400, "this user does not have the admin role")
}

func (r *UserPostgres) DeleteUser(userId int) error {
	query := fmt.Sprintf(`DELETE FROM %s WHERE id = $1`, database.UserTable)
	_, err := r.db.Exec(query, userId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return domain.NewErrorResponse(404, "user not found")
		}

		return err
	}

	return nil
}

func (r *UserPostgres) CheckUsersRoles(userIds []int, role string) error {
	var exists int

	for _, userId := range userIds {
		queryGetUser := fmt.Sprintf(`
		SELECT 1 FROM %s us
		WHERE us.id = $1 AND EXISTS (
			SELECT 1
			FROM %s AS us_rl
			LEFT JOIN %s AS rl ON rl.id = us_rl.role_id
			WHERE us_rl.users_id = us.id AND rl.name = '%s'
		)`, database.UserTable, database.UsersRoleTable, database.RoleTable, role)
		err := r.db.Get(&exists, queryGetUser, userId)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return domain.NewErrorResponse(404, fmt.Sprintf("user with this id = %d and with role = %s was not found", userId, role))
			}

			return err
		}
	}

	return nil
}

func (r *UserPostgres) GetUserByPhone(phone string) (models.Users, error) {
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
		WHERE us.phone = $1`, database.UsersRoleTable, database.RoleTable, database.UserTable)

	row := r.db.QueryRow(query, phone)
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

	return user, nil
}
