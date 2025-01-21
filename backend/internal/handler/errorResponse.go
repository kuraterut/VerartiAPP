package handler

import (
	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
	"verarti/internal"
)

type responseModel struct {
	Response any `json:"response"`
}

type dataModel struct {
	Data any `json:"data"`
}

type errorModel struct {
	Err any `json:"error"`
}

func newErrorResponse(c *gin.Context, statusCode int, message string) {
	logrus.Error(message)
	c.AbortWithStatusJSON(statusCode, errorModel{
		internal.ErrorResponse{
			Code: statusCode,
			Text: message,
		},
	})
}
