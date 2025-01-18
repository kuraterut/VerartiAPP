package handler

import (
	"github.com/gin-gonic/gin"
	"io"
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

	fileHeader, err := c.FormFile("photo")
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	file, err := fileHeader.Open()
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}
	defer file.Close()

	fileBytes, err := io.ReadAll(file)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	err = h.services.Profile.UpdatePhoto(userId, fileBytes)
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, "ok")
}

func (h *Handler) updateInfo(c *gin.Context) {}

func (h *Handler) updatePassword(c *gin.Context) {}
