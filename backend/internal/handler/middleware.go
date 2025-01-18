package handler

import (
	"errors"
	"github.com/gin-gonic/gin"
	"net/http"
	"strings"
)

const (
	authorizationHeader = "Authorization"
	userCtx             = "userId"
	rolesCtx            = "roles"
)

func (h *Handler) userIdentity(c *gin.Context) {
	header := c.GetHeader(authorizationHeader)
	if header == "" {
		newErrorResponse(c, http.StatusUnauthorized, "empty auth header")
		return
	}

	headerParts := strings.Split(header, " ")
	if len(headerParts) != 2 {
		newErrorResponse(c, http.StatusUnauthorized, "invalid auth header")
		return
	}

	if headerParts[0] != "Bearer" {
		newErrorResponse(c, http.StatusUnauthorized, "invalid auth header")
		return
	}

	if headerParts[1] == "" {
		newErrorResponse(c, http.StatusUnauthorized, "token is empty")
		return
	}

	userId, roles, err := h.services.Authorization.ParseToken(headerParts[1])
	if err != nil {
		newErrorResponse(c, http.StatusUnauthorized, err.Error())
		return
	}

	c.Set(rolesCtx, roles)
	c.Set(userCtx, userId)
}

func (h *Handler) masterIdentity(c *gin.Context) {
	roles, ok := c.Get(rolesCtx)
	if !ok {
		newErrorResponse(c, http.StatusInternalServerError, "roles not found")
		return
	}

	rolesStr, ok := roles.([]string)
	if !ok {
		newErrorResponse(c, http.StatusInternalServerError, "roles is of invalid type")
		return
	}

	var exist bool
	for _, role := range rolesStr {
		if role == "master" {
			exist = true
			break
		}
	}

	if !exist {
		newErrorResponse(c, http.StatusUnauthorized, "you have other access rights")
		return
	}
}

func (h *Handler) adminIdentity(c *gin.Context) {
	roles, ok := c.Get(rolesCtx)
	if !ok {
		newErrorResponse(c, http.StatusInternalServerError, "roles not found")
		return
	}

	rolesStr, ok := roles.([]string)
	if !ok {
		newErrorResponse(c, http.StatusInternalServerError, "roles is of invalid type")
		return
	}

	var exist bool
	for _, role := range rolesStr {
		if role == "admin" {
			exist = true
			break
		}
	}

	if !exist {
		newErrorResponse(c, http.StatusUnauthorized, "you have other access rights")
		return
	}
}

func (h *Handler) directorIdentity(c *gin.Context) {
	roles, ok := c.Get(rolesCtx)
	if !ok {
		newErrorResponse(c, http.StatusInternalServerError, "roles not found")
		return
	}

	rolesStr, ok := roles.([]string)
	if !ok {
		newErrorResponse(c, http.StatusInternalServerError, "roles is of invalid type")
		return
	}

	var exist bool
	for _, role := range rolesStr {
		if role == "director" {
			exist = true
			break
		}
	}

	if !exist {
		newErrorResponse(c, http.StatusUnauthorized, "you have other access rights")
		return
	}
}

func getUserId(c *gin.Context) (int, error) {
	id, ok := c.Get(userCtx)
	if !ok {
		newErrorResponse(c, http.StatusInternalServerError, "user id not found")
		return 0, errors.New("user id not found")
	}

	idInt, ok := id.(int)
	if !ok {
		newErrorResponse(c, http.StatusInternalServerError, "user id is of invalid type")
		return 0, errors.New("user id is of invalid type")
	}

	return idInt, nil
}
