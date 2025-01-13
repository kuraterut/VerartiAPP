package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

// todo сделать получение фото как url не как строку base64

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

func (h *Handler) updatePhoto(c *gin.Context) {}

func (h *Handler) updateInfo(c *gin.Context) {}

func (h *Handler) updatePassword(c *gin.Context) {}
