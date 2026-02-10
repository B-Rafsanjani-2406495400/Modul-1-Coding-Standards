# AdvProg Module 1: Coding Standards

## Reflection 1

### 1. Clean Code Principles Applied

Dalam pengembangan fitur **Edit** dan **Delete** pada aplikasi Eshop ini, saya telah menerapkan beberapa prinsip *Clean Code* untuk memastikan kode mudah dibaca dan dipelihara:

* **Meaningful Names (Penamaan yang Bermakna):**
  Saya menggunakan nama yang jelas dan deskriptif untuk kelas, metode, dan variabel.
    * *Classes:* `ProductController`, `ProductService`, dan `ProductRepository` secara jelas menunjukkan peran mereka dalam arsitektur MVC.
    * *Methods:* Nama seperti `findAll()`, `findById()`, dan `delete()` sangat deskriptif (self-explanatory). Saya melakukan refactoring nama method `deleteProduct` menjadi `delete` di dalam interface `ProductService` untuk mengurangi redundansi, karena konteksnya sudah jelas berada di dalam kelas yang menangani Product.

* **Single Responsibility Principle (SRP):**
  Saya memastikan bahwa setiap kelas memiliki tanggung jawab yang terpisah dan jelas:
    * **Controller:** `ProductController` sekarang hanya bertanggung jawab untuk menangani request HTTP dan mengarahkan ke view Thymeleaf. Controller mendelegasikan seluruh logika bisnis ke lapisan Service.
    * **Service:** `ProductServiceImpl` menangani logika bisnis (seperti pembuatan UUID, pencarian produk).
    * **Repository:** `ProductRepository` hanya berfokus pada manipulasi data (manajemen list in-memory).
    * *Perbaikan:* Saya memperbaiki pelanggaran prinsip ini di mana sebelumnya Controller mengakses Repository secara langsung (yang menyebabkan `NullPointerException`). Sekarang alurnya sudah benar: Controller -> Service -> Repository.

* **Function Structure (Struktur Fungsi):**
  Saya menjaga fungsi agar tetap kecil dan fokus. Contohnya, method `delete` pada Repository menggunakan `removeIf` (Java 8 lambda) daripada menggunakan looping manual yang kompleks. Hal ini membuat method lebih ringkas dan menghindari potensi error `ConcurrentModificationException`.

### 2. Secure Coding Practices Applied

Meskipun aplikasi ini masih sederhana, saya telah menerapkan praktik keamanan dasar:

* **UUID for ID Generation:**
  Alih-alih menggunakan integer berurutan (1, 2, 3...) untuk ID Produk, saya mengimplementasikan `UUID.randomUUID()`. Hal ini membuat ID sumber daya (resource) menjadi tidak dapat diprediksi, sehingga mengurangi risiko serangan *Insecure Direct Object Reference* (IDOR) di mana penyerang bisa menebak ID produk lain.

### 3. Areas for Improvement (Mistakes & Fixes)

Selama proses pengembangan, saya menghadapi beberapa masalah dan mengidentifikasi area yang perlu diperbaiki di masa depan:

* **Mistake: Logika yang salah pada Controller:**
  Awalnya, saya mencoba memanggil `repo.findById(id)` langsung di dalam `ProductController`. Hal ini menyebabkan `NullPointerException` karena repository belum di-inject ke dalam controller.
  **Fix:** Saya menghapus dependensi repository dari controller dan membuat method `findById` di `ProductService`, sehingga mematuhi arsitektur layer yang benar.

* **Mistake: Path Variable Mapping:**
  Saya mengalami error 404 saat mensubmit form Edit karena atribut `th:action` tidak menyertakan path variable ID.
  **Fix:** Saya memperbarui template Thymeleaf untuk menyertakan ID secara eksplisit pada action post: `th:action="@{/product/edit/{id}(id=${product.productId})}"`.

* **Future Improvement: Input Validation:**
  Saat ini, belum ada validasi untuk nama produk atau kuantitas (contoh: kuantitas negatif masih bisa diinput). Pada iterasi selanjutnya, saya berencana mengimplementasikan *Bean Validation* menggunakan anotasi seperti `@NotBlank` dan `@Min(0)` pada model, serta `@Valid` pada controller.