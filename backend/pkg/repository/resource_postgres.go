package repository

import (
	"fmt"
	"github.com/jmoiron/sqlx"
	"verarti/models"
)

type ResourcePostgres struct {
	db *sqlx.DB
}

func NewResourcePostgres(db *sqlx.DB) *ResourcePostgres {
	return &ResourcePostgres{db: db}
}

func (r *ResourcePostgres) Create(resource models.Resource) (int, error) {
	var id int
	createResourceQuery := fmt.Sprintf("INSERT INTO %s (name, description)"+
		"VALUES ($1, $2) RETURNING id", resourceTable)
	row := r.db.QueryRow(createResourceQuery, resource.Name, resource.Description)
	if err := row.Scan(&id); err != nil {
		return 0, err
	}

	return id, nil
}

func (r *ResourcePostgres) GetAll() ([]models.Resource, error) {
	var resources []models.Resource
	query := fmt.Sprintf("Select * from %s", resourceTable)
	err := r.db.Select(&resources, query)

	return resources, err
}
