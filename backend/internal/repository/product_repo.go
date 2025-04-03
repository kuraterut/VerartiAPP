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

type ProductPostgres struct {
	db *sqlx.DB
}

func NewProductPostgres(db *sqlx.DB) *ProductPostgres {
	return &ProductPostgres{db: db}
}

func (r *ProductPostgres) Create(product models.Product) (int, error) {
	var id int
	createProductQuery := fmt.Sprintf("INSERT INTO %s (name, price, count)"+
		"VALUES ($1, $2, $3) RETURNING id", database.ProductTable)
	row := r.db.QueryRow(createProductQuery, product.Name, product.Price, product.Count)
	if err := row.Scan(&id); err != nil {
		var pqErr *pq.Error
		if errors.As(err, &pqErr) {
			if pqErr.Code == "23505" {
				if strings.Contains(pqErr.Message, "name") {
					return 0, domain.NewErrorResponse(409, "product with this name already exists")
				}
			}
		}

		return 0, err
	}

	return id, nil
}

func (r *ProductPostgres) GetAll() ([]models.Product, error) {
	var products []models.Product
	query := fmt.Sprintf("SELECT * FROM %s", database.ProductTable)
	err := r.db.Select(&products, query)

	if len(products) == 0 {
		return []models.Product{}, nil
	}

	return products, err
}

func (r *ProductPostgres) GetById(productId int) (models.Product, error) {
	var product models.Product
	query := fmt.Sprintf("SELECT * FROM %s WHERE id = $1", database.ProductTable)
	err := r.db.Get(&product, query, productId)
	if errors.Is(err, sql.ErrNoRows) {
		return models.Product{}, domain.NewErrorResponse(404, "product not found")
	}

	return product, err
}

func (r *ProductPostgres) UpdateProduct(productId int, newProduct models.ProductUpdate) error {
	setValues := make([]string, 0)
	args := make([]interface{}, 0)
	argId := 1

	if newProduct.Name != nil {
		setValues = append(setValues, fmt.Sprintf("name=$%d", argId))
		args = append(args, *newProduct.Name)
		argId++
	}

	if newProduct.Price != nil {
		setValues = append(setValues, fmt.Sprintf("price=$%d", argId))
		args = append(args, *newProduct.Price)
		argId++
	}

	if newProduct.Count != nil {
		setValues = append(setValues, fmt.Sprintf("count=$%d", argId))
		args = append(args, *newProduct.Count)
		argId++
	}

	setQuery := strings.Join(setValues, ", ")

	query := fmt.Sprintf("UPDATE %s SET %s WHERE id = $%d",
		database.ProductTable, setQuery, argId)
	args = append(args, productId)

	_, err := r.db.Exec(query, args...)
	return err
}

func (r *ProductPostgres) DeleteProduct(productId int) error {
	query := fmt.Sprintf(`DELETE FROM %s WHERE id = $1`, database.ProductTable)
	_, err := r.db.Exec(query, productId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil
		}

		return err
	}

	return nil
}

func (r *ProductPostgres) CheckingProductsExistence(productIds []int) error {
	var exists int

	for _, productId := range productIds {
		queryGetProduct := fmt.Sprintf(`SELECT 1 FROM %s WHERE id = $1`, database.ProductTable)
		err := r.db.Get(&exists, queryGetProduct, productId)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return domain.NewErrorResponse(404, fmt.Sprintf("product with this id = %d was not found", productId))
			}

			return err
		}
	}

	return nil
}
