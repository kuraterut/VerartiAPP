package database

import (
	"github.com/minio/minio-go/v6"
	"os"
)

func InitializeMinioClient() (*minio.Client, error) {
	return minio.New(os.Getenv("AWS_ENDPOINT"), os.Getenv("AWS_ACCESS_KEY_ID"),
		os.Getenv("AWS_SECRET_ACCESS_KEY"), false)
}
