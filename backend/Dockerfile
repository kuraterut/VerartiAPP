FROM golang:1.22 AS builder

RUN go version
ENV GOPATH=/

COPY ./ ./

RUN apt-get update
RUN apt-get -y install postgresql-client

RUN chmod +x wait-for-postgres.sh

RUN go mod download
RUN go build -o verarti-app ./cmd/main.go

CMD ["./verarti-app"]