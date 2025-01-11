FROM golang:1.22 AS builder

RUN go version
ENV GOPATH=/

COPY ./ ./

RUN apt-get update && \
    apt-get -y install postgresql-client && \
    curl -L https://github.com/golang-migrate/migrate/releases/download/v4.15.2/migrate.linux-amd64.tar.gz | tar xvz && \
    mv migrate /usr/local/bin/

RUN go mod download
RUN go build -o verarti-app ./cmd/main.go

CMD ["./verarti-app"]