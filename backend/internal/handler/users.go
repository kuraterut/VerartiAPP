package handler

import (
	"errors"
	"github.com/gin-gonic/gin"
	"net/http"
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

func (h *Handler) getMasterById(c *gin.Context) {}

func (h *Handler) getAllAdmins(c *gin.Context) {}

func (h *Handler) getAdminById(c *gin.Context) {}

func (h *Handler) getDirector(c *gin.Context) {}

func (h *Handler) deleteUser(c *gin.Context) {}
