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
		return models.Client{}, internal.NewErrorResponse(404, "client with this phone number not found")
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
		return models.Client{}, internal.NewErrorResponse(404, "client with this id not found")
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

	if errors.Is(err, sql.ErrNoRows) {
		return nil, internal.NewErrorResponse(404, "no clients found")
	}

	return clients, err
}
