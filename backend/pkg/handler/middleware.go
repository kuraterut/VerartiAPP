package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"strings"
)

const (
	authorizationHeader = "Authorization"
	userCtx             = "userId"
	roleCtx             = "role"
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

	userId, role, err := h.services.Authorization.ParseToken(headerParts[1])
	if err != nil {
		newErrorResponse(c, http.StatusUnauthorized, err.Error())
		return
	}

	c.Set(roleCtx, role)
	c.Set(userCtx, userId)
}

func (h *Handler) masterIdentity(c *gin.Context) {
	role, _ := c.Get(roleCtx)

	if role != "master" {
		newErrorResponse(c, http.StatusUnauthorized, "you have other access rights")
		return
	}
}

func (h *Handler) adminIdentity(c *gin.Context) {
	role, _ := c.Get(roleCtx)

	if role != "admin" {
		newErrorResponse(c, http.StatusUnauthorized, "you have other access rights")
		return
	}
}

func (h *Handler) directorIdentity(c *gin.Context) {
	role, _ := c.Get(roleCtx)

	if role != "director" {
		newErrorResponse(c, http.StatusUnauthorized, "you have other access rights")
		return
	}
}
