package handler

import (
	"errors"
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
	"verarti/internal"
)

func (h *Handler) getAllMasters(c *gin.Context) {
	masters, err := h.services.User.GetAllMasters()
	if err != nil {
		var errResp *internal.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{
		"masters": masters,
	})
}

func (h *Handler) getMasterById(c *gin.Context) {
	masterId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	master, err := h.services.User.GetMasterById(masterId)
	if err != nil {
		var errResp *internal.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, master)
}

func (h *Handler) getAllAdmins(c *gin.Context) {
	admins, err := h.services.User.GetAllAdmins()
	if err != nil {
		var errResp *internal.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{
		"admins": admins,
	})
}

func (h *Handler) getAdminById(c *gin.Context) {
	adminId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	admin, err := h.services.User.GetAdminById(adminId)
	if err != nil {
		var errResp *internal.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, admin)
}

func (h *Handler) getDirector(c *gin.Context) {
	director, err := h.services.User.GetDirector()
	if err != nil {
		var errResp *internal.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, director)
}

func (h *Handler) deleteUser(c *gin.Context) {
	userId, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid is param")
		return
	}

	err = h.services.User.DeleteUser(userId)
	if err != nil {
		var errResp *internal.ErrorResponse
		if errors.As(err, &errResp) {
			newErrorResponse(c, errResp.Code, errResp.Text)
			return
		}

		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, "OK")
}
