package pkg

type ErrorResponse struct {
	Code int    `json:"code"`
	Text string `json:"message"`
}

func (e *ErrorResponse) Error() string {
	return e.Text
}

func NewErrorResponse(code int, text string) *ErrorResponse {
	return &ErrorResponse{Code: code, Text: text}
}
