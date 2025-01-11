package repository

import (
	"errors"
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

func (r *ResourcePostgres) GetById(resourceId int) (models.Resource, error) {
	var resource models.Resource
	query := fmt.Sprintf("Select * from %s WHERE id = $1", resourceTable)
	err := r.db.Get(&resource, query, resourceId)

	return resource, err
}

func (r *ResourcePostgres) GetByMasterId(masterId int) ([]models.Resource, error) {
	var resources []models.Resource
	query := fmt.Sprintf("Select res.id, res.name, res.description from %s us "+
		" INNER JOIN %s us_res on us.id = us_res.users_id"+
		" INNER JOIN %s res on us_res.resource_id = res.id"+
		" WHERE us.id = $1", userTable, userResourceTable, resourceTable)
	err := r.db.Select(&resources, query, masterId)

	return resources, err
}

func (r *ResourcePostgres) Add(masterId, resourceId int) (int, error) {
	var id int

	query := fmt.Sprintf("Select id from %s WHERE users_id = $1 AND resource_id = $2", userResourceTable)
	err := r.db.Get(&id, query, masterId, resourceId)
	if err != nil {
		return 0, errors.New("")
	}

	addResourceQuery := fmt.Sprintf("INSERT INTO %s (users_id, resource_id)"+
		"VALUES ($1, $2) RETURNING id", userResourceTable)
	row := r.db.QueryRow(addResourceQuery, masterId, resourceId)
	if err = row.Scan(&id); err != nil {
		return 0, err
	}

	return id, nil
}
