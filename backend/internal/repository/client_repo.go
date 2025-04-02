package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"strings"
	"verarti/internal/domain"
	"verarti/models"
	"verarti/pkg/database"
)

type ClientPostgres struct {
	db *sqlx.DB
}

func NewClientPostgres(db *sqlx.DB) *ClientPostgres {
	return &ClientPostgres{db: db}
}

func (r *ClientPostgres) CreateClient(client models.Client) (int, error) {
	var id int

	if client.Birthday == "" {
		client.Birthday = "0001-01-01"
	}

	queryCreateUser := fmt.Sprintf("INSERT INTO %s (name, surname, patronymic, email, phone, comment, birthday)"+
		" VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING id", database.ClientTable)
	row := r.db.QueryRow(queryCreateUser, client.Name, client.Surname, client.Patronymic,
		client.Email, client.Phone, client.Comment, client.Birthday)
	if err := row.Scan(&id); err != nil {
		return 0, err
	}

	return id, nil
}

func (r *ClientPostgres) GetClientByPhone(phone string) (models.Client, error) {
	var client models.Client

	query := fmt.Sprintf("SELECT id, name, surname, patronymic, email, phone, comment, TO_CHAR(birthday, 'YYYY-MM-DD') AS birthday FROM %s WHERE phone=$1", database.ClientTable)
	err := r.db.Get(&client, query, phone)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		return models.Client{}, err
	}

	if errors.Is(err, sql.ErrNoRows) {
		return models.Client{}, domain.NewErrorResponse(404, "client with this phone number not found")
	}

	return client, err
}

func (r *ClientPostgres) GetClientById(clientId int) (models.Client, error) {
	var client models.Client

	query := fmt.Sprintf("SELECT id, name, surname, patronymic, email, phone, comment, TO_CHAR(birthday, 'YYYY-MM-DD') AS birthday FROM %s WHERE id=$1", database.ClientTable)
	err := r.db.Get(&client, query, clientId)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		return models.Client{}, err
	}

	if errors.Is(err, sql.ErrNoRows) {
		return models.Client{}, domain.NewErrorResponse(404, "client with this id not found")
	}

	return client, err
}

func (r *ClientPostgres) GetAllClients() ([]models.Client, error) {
	var clients []models.Client

	query := fmt.Sprintf("SELECT id, name, surname, patronymic, email, phone, comment, TO_CHAR(birthday, 'YYYY-MM-DD') AS birthday FROM %s", database.ClientTable)
	err := r.db.Select(&clients, query)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		return nil, err
	}

	if len(clients) == 0 {
		return []models.Client{}, nil
	}

	return clients, err
}

func (r *ClientPostgres) UpdateClient(clientId int, input models.Client) error {
	setValues := make([]string, 0)
	args := make([]interface{}, 0)
	argId := 1

	if input.Name != "" {
		setValues = append(setValues, fmt.Sprintf("name=$%d", argId))
		args = append(args, input.Name)
		argId++
	}

	if input.Surname != "" {
		setValues = append(setValues, fmt.Sprintf("surname=$%d", argId))
		args = append(args, input.Surname)
		argId++
	}

	if input.Patronymic != "" {
		setValues = append(setValues, fmt.Sprintf("patronymic=$%d", argId))
		args = append(args, input.Patronymic)
		argId++
	}

	if input.Email != "" {
		setValues = append(setValues, fmt.Sprintf("email=$%d", argId))
		args = append(args, input.Email)
		argId++
	}

	if input.Phone != "" {
		setValues = append(setValues, fmt.Sprintf("phone=$%d", argId))
		args = append(args, input.Phone)
		argId++
	}

	if input.Comment != "" {
		setValues = append(setValues, fmt.Sprintf("comment=$%d", argId))
		args = append(args, input.Comment)
		argId++
	}

	if input.Birthday != "" {
		setValues = append(setValues, fmt.Sprintf("birthday=$%d", argId))
		args = append(args, input.Birthday)
		argId++
	}

	setQuery := strings.Join(setValues, ", ")

	if setQuery == "" {
		return domain.NewErrorResponse(400, "invalid input body: it is empty")
	}

	query := fmt.Sprintf("UPDATE %s SET %s WHERE id = $%d",
		database.ClientTable, setQuery, argId)
	args = append(args, clientId)

	_, err := r.db.Exec(query, args...)
	return err
}

func (r *ClientPostgres) CheckingClientsExistence(clientIds []int) error {
	var exists int

	for _, clientId := range clientIds {
		queryGetClient := fmt.Sprintf(`SELECT 1 FROM %s WHERE id = $1`, database.ClientTable)
		err := r.db.Get(&exists, queryGetClient, clientId)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return domain.NewErrorResponse(404, fmt.Sprintf("client with this id = %d was not found", clientId))
			}

			return err
		}
	}

	return nil
}
