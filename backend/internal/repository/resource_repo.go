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

type ResourcePostgres struct {
	db *sqlx.DB
}

func NewResourcePostgres(db *sqlx.DB) *ResourcePostgres {
	return &ResourcePostgres{db: db}
}

func (r *ResourcePostgres) Create(resource models.Resource) (int, error) {
	var id int
	createResourceQuery := fmt.Sprintf("INSERT INTO %s (name, description)"+
		"VALUES ($1, $2) RETURNING id", database.ResourceTable)
	row := r.db.QueryRow(createResourceQuery, resource.Name, resource.Description)
	if err := row.Scan(&id); err != nil {
		return 0, err
	}

	return id, nil
}

func (r *ResourcePostgres) GetAll() ([]models.Resource, error) {
	var resources []models.Resource
	query := fmt.Sprintf("Select * from %s", database.ResourceTable)
	err := r.db.Select(&resources, query)

	return resources, err
}

func (r *ResourcePostgres) GetById(resourceId int) (models.Resource, error) {
	var resource models.Resource
	query := fmt.Sprintf("Select * from %s WHERE id = $1", database.ResourceTable)
	err := r.db.Get(&resource, query, resourceId)
	if errors.Is(err, sql.ErrNoRows) {
		return models.Resource{}, internal.NewErrorResponse(404, "resource not found")
	}

	return resource, err
}

func (r *ResourcePostgres) GetByMasterId(masterId int) ([]models.Resource, error) {
	var resources []models.Resource
	query := fmt.Sprintf("Select res.id, res.name, res.description from %s us "+
		" INNER JOIN %s us_res on us.id = us_res.users_id"+
		" INNER JOIN %s res on us_res.resource_id = res.id"+
		" WHERE us.id = $1", database.UserTable, database.UserResourceTable, database.ResourceTable)
	err := r.db.Select(&resources, query, masterId)

	return resources, err
}

func (r *ResourcePostgres) Add(masterId, resourceId int) (int, error) {
	var id int

	var resource models.Resource
	query := fmt.Sprintf("Select * from %s WHERE id = $1", database.ResourceTable)
	err := r.db.Get(&resource, query, resourceId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return 0, internal.NewErrorResponse(400, "adding a non-existent resource")
		}

		return 0, err
	}

	query = fmt.Sprintf("Select id from %s WHERE users_id = $1 AND resource_id = $2", database.UserResourceTable)
	err = r.db.Get(&id, query, masterId, resourceId)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		return 0, err
	}

	if id != 0 {
		return id, nil
	}

	addResourceQuery := fmt.Sprintf("INSERT INTO %s (users_id, resource_id)"+
		"VALUES ($1, $2) RETURNING id", database.UserResourceTable)
	row := r.db.QueryRow(addResourceQuery, masterId, resourceId)
	if err = row.Scan(&id); err != nil {
		return 0, err
	}

	return id, nil
}
