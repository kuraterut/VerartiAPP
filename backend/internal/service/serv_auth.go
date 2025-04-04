package service

import (
	"crypto/sha1"
	"errors"
	"fmt"
	"github.com/dgrijalva/jwt-go"
	"time"
	"verarti/internal/domain"
	"verarti/internal/repository"
	"verarti/models"
)

const (
	salt       = "nsvnjnu4u8948vuh29hrhfebv8394hvub3u"
	signingKey = "sdij84uuv428hjkdnu4hvjnribvhiu"
	tokenTTl   = 12 * time.Hour
)

type tokenClaims struct {
	jwt.StandardClaims
	UserId int    `json:"user_id"`
	Role   string `json:"user_role"`
}

type AuthService struct {
	repo repository.Authorization
}

func NewAuthService(repo repository.Authorization) *AuthService {
	return &AuthService{repo: repo}
}

func (s *AuthService) CreateUser(user models.Users, roleIds []int) (int, error) {
	user.Password = generatePasswordHash(user.Password)

	return s.repo.CreateUser(user, roleIds)
}

func generatePasswordHash(password string) string {
	hash := sha1.New()
	hash.Write([]byte(password))

	return fmt.Sprintf("%x", hash.Sum([]byte(salt)))
}

func (s *AuthService) GenerateToken(phone, password, role string) (string, error) {
	user, err := s.repo.GetUser(phone, generatePasswordHash(password))
	if err != nil {
		return "", err
	}

	var exist bool
	for _, userRole := range user.Roles {
		if userRole == role {
			exist = true
			break
		}
	}
	if !exist {
		return "", domain.NewErrorResponse(403, "this user does not have such a role")
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, &tokenClaims{
		jwt.StandardClaims{
			ExpiresAt: time.Now().Add(tokenTTl).Unix(),
			IssuedAt:  time.Now().Unix(),
		},
		user.Id,
		role,
	})

	res, err := token.SignedString([]byte(signingKey))
	return res, err
}

func (s *AuthService) ParseToken(accessToken string) (int, string, error) {
	token, err := jwt.ParseWithClaims(accessToken, &tokenClaims{}, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, errors.New("invalid signing method")
		}

		return []byte(signingKey), nil
	})
	if err != nil {
		return 0, "", err
	}

	claims, ok := token.Claims.(*tokenClaims)
	if !ok {
		return 0, "", errors.New("token claims are not of type *tokenClaims")
	}

	return claims.UserId, claims.Role, nil
}
