package handler

import (
	"github.com/gin-gonic/gin"
	"verarti/internal/service"
)

type Handler struct {
	services *service.Service
}

func NewHandler(services *service.Service) *Handler {
	return &Handler{services: services}
}

func (h *Handler) InitRoutes() *gin.Engine {
	router := gin.New()

	auth := router.Group("/auth")
	{
		auth.POST("/signin", h.signIn)
	}

	api := router.Group("/api", h.userIdentity)
	{
		master := api.Group("/master", h.masterIdentity)
		{
			profile := master.Group("/profile")
			{
				profile.GET("/", h.getUserInfo)
				profile.PUT("/photo", h.updatePhoto)
				profile.PUT("/info", h.updateInfo)
				profile.PUT("/password", h.updatePassword)
			}

			resource := master.Group("/resource")
			{
				resource.GET("/", h.getResourcesByMasterId)
				resource.GET("/all", h.getAllResources)
				resource.GET("/:id", h.getResourceById)
				resource.POST("/:id", h.addResource)
				resource.POST("/request", h.createRequest)
				resource.GET("/request", h.getRequests)
				resource.GET("/response", h.getResponseByRequestId)
			}

			schedule := master.Group("/schedule")
			{
				schedule.GET("/day", h.getDailySchedule)
				schedule.GET("/month", h.getMonthlySchedule)
				schedule.POST("/", h.cancellationRequest)
			}

			appointment := master.Group("/appointment")
			{
				appointment.GET("/", h.getAllAppointments)
				appointment.GET("/:id", h.getAppointmentById)
			}

			users := master.Group("/users")
			{
				users.GET("/master", h.getAllMasters)
				users.GET("/master/:id", h.getMasterById)
				users.GET("/admin", h.getAllAdmins)
				users.GET("/admin/:id", h.getAdminById)
				users.GET("/director", h.getDirector)
			}

			clients := master.Group("/clients")
			{
				clients.GET("/", h.getAllClients)
				clients.GET("/:id", h.getClientById)
				clients.GET("/phone", h.getClientByPhone)
			}

			feedback := master.Group("/feedback")
			{
				feedback.GET("/", h.getAllFeedbacks)
				feedback.GET("/:id", h.getFeedbackById)
				feedback.PATCH("/", h.sortFeedbacks) // todo не помню зачем это нужно, мб поменять тип запроса
			}

		}

		admin := api.Group("/admin", h.adminIdentity)
		{
			users := admin.Group("/users")
			{
				users.GET("/master", h.getAllMasters)
				users.GET("/master/:id", h.getMasterById)
				users.GET("/admin", h.getAllAdmins)
				users.GET("/admin/:id", h.getAdminById)
				users.GET("/director", h.getDirector)
			}

			clients := admin.Group("/clients")
			{
				clients.POST("/", h.createClient)
				clients.GET("/", h.getAllClients)
				clients.GET("/:id", h.getClientById)
				clients.GET("/phone", h.getClientByPhone)
				clients.PUT("/:id", h.updateClient)
			}

			feedback := admin.Group("/feedback")
			{
				feedback.GET("/")
				feedback.GET("/:id")
				feedback.POST("/")
			}

			resource := admin.Group("/resource")
			{
				resource.POST("/", h.createResource)
				resource.GET("/", h.getAllResources)
				resource.GET("/:id", h.getResourceById)
				//resource.POST("/response", h.createResponse) // todo
				//resource.GET("/request", h.getRequests)
				//resource.GET("/response", h.getResponseByRequestId)
			}

			appointment := admin.Group("/appointment")
			{
				appointment.POST("/", h.createAppointment)
				appointment.POST("/:id", h.addAppointmentForMaster)
				appointment.GET("/", h.getAllAppointments)
				appointment.GET("/:id", h.getAppointmentById)
				appointment.PUT("/:id", h.updateAppointment)
				appointment.DELETE("/:id", h.deleteAppointment)
			}
		}

		director := api.Group("/director", h.directorIdentity)
		{
			users := director.Group("/users")
			{
				users.POST("/signup", h.signUp)
				users.GET("/master", h.getAllMasters)
				users.GET("/master/:id", h.getMasterById)
				users.GET("/admin", h.getAllAdmins)
				users.GET("/admin/:id", h.getAdminById)
				users.GET("/director", h.getDirector)
				users.DELETE("/:id", h.deleteUser)
			}

			clients := director.Group("/clients")
			{
				clients.GET("/")
				clients.GET("/:id")
			}

			feedback := director.Group("/feedback")
			{
				feedback.GET("/")
				feedback.GET("/:id")
			}
		}
	}

	return router
}
