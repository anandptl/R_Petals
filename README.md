# 🌸 R_Petals Backend

R_Petals is a production-oriented backend for a flower/product delivery platform.

The backend is built using **Spring Boot**, **Spring Security**, **JWT Authentication**, **MongoDB**, and **DTO-based REST APIs**.

The system supports users, shops, products, categories, subcategories, addresses, OTP-based authentication, shop-product mapping, and role-based authorization.

---

## 🚀 Features

- 📱 Mobile OTP Authentication
- 📧 Email OTP Verification
- 🔐 JWT Authentication
- 👤 User Management
- 🏪 Shop Registration
- 👨‍💼 Shopkeeper Management
- 👑 Admin Role Authorization
- 📦 Product Management
- 🗂️ Category Management
- 📁 SubCategory Management
- 📍 User Address Management
- 🛍️ Shop Product Mapping
- 🚚 Nearest Shop Product Matching
- 🛡️ Global Exception Handling
- ✅ DTO Validation
- 🌐 CORS Configuration
- 🔒 Stateless Spring Security
- 📄 Standardized API Responses

---

# 🏗️ System Architecture

```text
                    Next.js Frontend
                         │
                         │ REST API / JSON
                         ▼
                ┌───────────────────┐
                │   Spring Boot     │
                │      Backend      │
                └─────────┬─────────┘
                          │
              ┌───────────┼───────────┐
              │           │           │
              ▼           ▼           ▼
         Controller     Service     Security
              │           │           │
              ▼           ▼           ▼
             DTO       Repository     JWT
                          │
                          ▼
                      MongoDB
