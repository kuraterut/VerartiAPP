package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

func (h *Handler) getUserInfo(c *gin.Context) {
	userId, err := getUserId(c)
	if err != nil {
		return
	}

	user, err := h.services.Profile.GetUserInfo(userId)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, user)
}

func (h *Handler) updatePhoto(c *gin.Context) {
	userId, err := getUserId(c)
	if err != nil {
		return
	}

	file, err := c.FormFile("photo")
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	err := h.services.Profile.
}

func (h *Handler) updateInfo(c *gin.Context) {}

func (h *Handler) updatePassword(c *gin.Context) {}
