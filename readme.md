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
*

# Reflection 2: Unit Testing & Code Coverage

## 1. Code Quality Analysis (Honest Review)

Setelah menulis Unit Test dan Functional Test untuk fitur `Create`, `Edit`, dan `Delete`, saya menyadari beberapa hal mengenai kualitas kode saya:

### A. Positif (Apa yang sudah baik)
1.  **Separation of Concerns:** Saya berhasil memisahkan tanggung jawab antara `Controller`, `Service`, dan `Repository`. Controller tidak lagi berisi logika bisnis, dan Repository fokus pada manajemen data.
2.  **Defensive Programming:** Pada method `edit` dan `delete` di Repository, saya telah menambahkan pengecekan `null` atau menggunakan `removeIf`. Hal ini mencegah `NullPointerException` dan membuat aplikasi lebih stabil.
3.  **Test Coverage:** Dengan menerapkan skenario positif dan negatif, saya berhasil mencapai coverage yang tinggi (100% pada method krusial). Ini memberikan jaminan bahwa logika dasar aplikasi berjalan sesuai harapan.

### B. Area for Improvement (Kekurangan yang perlu diperbaiki)
1.  **Code Duplication in Tests:** Saya menyadari adanya duplikasi kode setup pada Functional Test. Konfigurasi seperti setup `baseUrl`, `port`, dan inisialisasi WebDriver ditulis berulang kali di setiap file test (`CreateProductFunctionalTest`, dll). Ini melanggar prinsip *DRY (Don't Repeat Yourself)*.
2.  **Input Validation:** Meskipun logic berjalan, aplikasi saya masih rentan terhadap input tidak valid. Contohnya, belum ada validasi jika `productQuantity` diisi negatif atau `productName` kosong. Unit Test saat ini hanya mengecek alur logika, bukan validasi bisnis yang mendalam.
3.  **Error Handling:** Saat ini, jika produk tidak ditemukan (misal user memanipulasi URL ID), aplikasi mungkin hanya me-redirect atau tidak memberikan pesan error yang jelas kepada pengguna (User Experience masih kurang).

## 2. Reflection on Writing Tests

**1. Perasaan setelah menulis Unit Test:**
Menulis unit test memberikan rasa aman (*security*) dan percaya diri saat melakukan refactoring. Awalnya terasa melelahkan karena kode test bisa lebih panjang dari kode implementasi aslinya. Namun, ketika saya mengubah logika di `Repository` (misalnya mengganti loop manual dengan `removeIf`), unit test langsung memberitahu jika ada yang salah (Regression Testing). Ini sangat membantu menjaga kualitas kode.

**2. Berapa banyak unit test yang cukup?**
Tidak ada angka pasti, namun prinsipnya adalah setiap *branch* logika (if/else), *loop*, dan *exception handling* harus ter-cover. Namun, perlu diingat bahwa **100% Code Coverage tidak menjamin 100% Bug-Free**.
* Coverage hanya mengukur baris kode yang dieksekusi, bukan kebenaran logika bisnis.
* Contoh: Kode saya mungkin 100% covered, tapi jika saya lupa menangani logika "Stok barang tidak boleh negatif", test akan tetap hijau (passed) meskipun secara bisnis itu adalah bug.
* Oleh karena itu, kualitas test (menguji edge cases & boundary values) lebih penting daripada sekadar angka coverage.

**3. Kebersihan kode dalam Functional Test:**
Ketika membuat functional test baru untuk fitur lain, saya menyadari kode test saya menjadi tidak bersih karena banyak duplikasi (copy-paste) dari test sebelumnya.
* **Masalah:** Setup server, inisialisasi driver, dan definisi URL dasar diulang-ulang.
* **Solusi:** Seharusnya saya membuat **Base Test Class** yang menangani setup (`@BeforeEach`) dan teardown driver. Test class lain cukup melakukan *extends* ke kelas base tersebut. Ini akan membuat kode test lebih bersih dan mudah dipelihara.