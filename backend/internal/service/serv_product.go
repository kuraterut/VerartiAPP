package service

import (
	"verarti/internal/repository"
	"verarti/models"
)

type ProductService struct {
	repo repository.Product
}

func NewProductService(repo repository.Product) *ProductService {
	return &ProductService{repo: repo}
}

func (s *ProductService) Create(product models.Product) (int, error) {
	return s.repo.Create(product)
}

func (s *ProductService) GetAll() ([]models.Product, error) {
	return s.repo.GetAll()
}

func (s *ProductService) GetById(productId int) (models.Product, error) {
	return s.repo.GetById(productId)
}

func (s *ProductService) UpdateProduct(productId int, newProduct models.ProductUpdate) error {
	return s.repo.UpdateProduct(productId, newProduct)
}

func (s *ProductService) DeleteProduct(productId int) error {
	return s.repo.DeleteProduct(productId)
}
