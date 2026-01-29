# 🚀 Caching Proxy Server

A high-performance **HTTP caching proxy server** built using **Spring Boot** and **Redis**.  
The proxy forwards incoming client requests to a configurable origin server, caches responses in Redis, and serves repeated requests directly from cache to reduce latency and load on the origin server.

---

## 📌 Project Overview

This project implements a simple yet production-oriented caching proxy to demonstrate:

- HTTP request forwarding
- Server-side caching using Redis
- CLI-based configuration
- Dockerized backend services
- Clean backend architecture using Spring Boot

---

## ✨ Features

- 🔁 Proxies HTTP requests to an origin server
- ⚡ Caches responses using Redis
- ⏳ TTL-based cache expiration
- 🧹 Cache invalidation support
- 🧩 Configurable via command-line arguments
- 🐳 Docker-ready
- ☁️ Cloud deployable (AWS-ready)

---

## 🛠 Tech Stack

- Java 21  
- Spring Boot  
- Spring Data Redis  
- Jedis  
- Redis  
- Docker  

---

## ⚙️ How It Works

1. Client sends a request to the proxy server  
2. Proxy checks Redis for cached response  
3. If cache hit → response returned immediately  
4. If cache miss → request forwarded to origin server  
5. Response is cached in Redis with TTL  
6. Response returned to the client  

---

## ▶️ Getting Started

### 1️⃣ Start Redis (Using Docker)

```bash
docker run -d \
  --name redis \
  -p 6379:6379 \
  redis:7

## Project URL
https://github.com/mdaazam0786/Proxy-Server
