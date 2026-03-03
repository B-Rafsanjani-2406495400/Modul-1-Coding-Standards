## Module 1: Coding Standards

### Reflection 1

#### 1. Clean Code Principles Applied

Dalam pengembangan fitur **Edit** dan **Delete** pada aplikasi Eshop ini, saya telah menerapkan beberapa prinsip *Clean Code* untuk memastikan kode mudah dibaca dan dipelihara:

- **Meaningful Names (Penamaan yang Bermakna):**  
  Saya menggunakan nama yang jelas dan deskriptif untuk kelas, metode, dan variabel.
  - *Classes:* `ProductController`, `ProductService`, dan `ProductRepository` secara jelas menunjukkan peran mereka dalam arsitektur MVC.
  - *Methods:* Nama seperti `findAll()`, `findById()`, dan `delete()` sangat deskriptif (*self-explanatory*).  
    Saya melakukan refactoring nama method `deleteProduct` menjadi `delete` di dalam interface `ProductService` untuk mengurangi redundansi, karena konteksnya sudah jelas berada di dalam kelas yang menangani Product.

- **Single Responsibility Principle (SRP):**  
  Saya memastikan bahwa setiap kelas memiliki tanggung jawab yang terpisah dan jelas:
  - **Controller:** `ProductController` hanya bertanggung jawab menangani request HTTP dan mengarahkan ke view Thymeleaf. Controller mendelegasikan seluruh logika bisnis ke lapisan Service.
  - **Service:** `ProductServiceImpl` menangani logika bisnis (seperti pembuatan UUID, pencarian produk).
  - **Repository:** `ProductRepository` hanya berfokus pada manipulasi data (manajemen list in-memory).
  - *Perbaikan:* Saya memperbaiki pelanggaran prinsip ini di mana sebelumnya Controller mengakses Repository secara langsung (yang menyebabkan `NullPointerException`). Sekarang alurnya sudah benar: **Controller → Service → Repository**.

- **Function Structure (Struktur Fungsi):**  
  Saya menjaga fungsi agar tetap kecil dan fokus. Contohnya, method `delete` pada Repository menggunakan `removeIf` (Java 8 lambda) daripada menggunakan looping manual yang kompleks. Hal ini membuat method lebih ringkas dan menghindari potensi error `ConcurrentModificationException`.

#### 2. Secure Coding Practices Applied

Meskipun aplikasi ini masih sederhana, saya telah menerapkan praktik keamanan dasar:

- **UUID for ID Generation:**  
  Alih-alih menggunakan integer berurutan (1, 2, 3...) untuk ID Produk, saya mengimplementasikan `UUID.randomUUID()`. Hal ini membuat ID sumber daya (*resource*) menjadi tidak dapat diprediksi, sehingga mengurangi risiko serangan *Insecure Direct Object Reference* (IDOR) di mana penyerang bisa menebak ID produk lain.

#### 3. Areas for Improvement (Mistakes & Fixes)

Selama proses pengembangan, saya menghadapi beberapa masalah dan mengidentifikasi area yang perlu diperbaiki di masa depan:

- **Mistake: Logika yang salah pada Controller**  
  Awalnya, saya mencoba memanggil `repo.findById(id)` langsung di dalam `ProductController`. Hal ini menyebabkan `NullPointerException` karena repository belum di-*inject* ke dalam controller.  
  **Fix:** Saya menghapus dependensi repository dari controller dan membuat method `findById` di `ProductService`, sehingga mematuhi arsitektur layer yang benar.

- **Mistake: Path Variable Mapping**  
  Saya mengalami error 404 saat mensubmit form Edit karena atribut `th:action` tidak menyertakan path variable ID.  
  **Fix:** Saya memperbarui template Thymeleaf untuk menyertakan ID secara eksplisit pada action post:  
  `th:action="@{/product/edit/{id}(id=${product.productId})}"`.

- **Future Improvement: Input Validation**  
  Saat ini, belum ada validasi untuk nama produk atau kuantitas (contoh: kuantitas negatif masih bisa diinput). Pada iterasi selanjutnya, saya berencana mengimplementasikan *Bean Validation* menggunakan anotasi seperti `@NotBlank` dan `@Min(0)` pada model, serta `@Valid` pada controller.

---

### Reflection 2: Unit Testing & Code Coverage

#### 1. Code Quality Analysis (Honest Review)

Setelah menulis Unit Test dan Functional Test untuk fitur `Create`, `Edit`, dan `Delete`, saya menyadari beberapa hal mengenai kualitas kode saya:

**A. Positif (Apa yang sudah baik)**
1. **Separation of Concerns:** Saya berhasil memisahkan tanggung jawab antara `Controller`, `Service`, dan `Repository`. Controller tidak lagi berisi logika bisnis, dan Repository fokus pada manajemen data.
2. **Defensive Programming:** Pada method `edit` dan `delete` di Repository, saya telah menambahkan pengecekan `null` atau menggunakan `removeIf`. Hal ini mencegah `NullPointerException` dan membuat aplikasi lebih stabil.
3. **Test Coverage:** Dengan menerapkan skenario positif dan negatif, saya berhasil mencapai coverage yang tinggi (100% pada method krusial). Ini memberikan jaminan bahwa logika dasar aplikasi berjalan sesuai harapan.

**B. Area for Improvement (Kekurangan yang perlu diperbaiki)**
1. **Code Duplication in Tests:** Saya menyadari adanya duplikasi kode setup pada Functional Test. Konfigurasi seperti setup `baseUrl`, `port`, dan inisialisasi WebDriver ditulis berulang kali di setiap file test (`CreateProductFunctionalTest`, dll). Ini melanggar prinsip *DRY (Don't Repeat Yourself)*.
2. **Input Validation:** Meskipun logic berjalan, aplikasi saya masih rentan terhadap input tidak valid. Contohnya, belum ada validasi jika `productQuantity` diisi negatif atau `productName` kosong. Unit Test saat ini hanya mengecek alur logika, bukan validasi bisnis yang mendalam.
3. **Error Handling:** Saat ini, jika produk tidak ditemukan (misal user memanipulasi URL ID), aplikasi mungkin hanya me-redirect atau tidak memberikan pesan error yang jelas kepada pengguna (User Experience masih kurang).

#### 2. Reflection on Writing Tests

**1. Perasaan setelah menulis Unit Test**  
Menulis unit test memberikan rasa aman (*security*) dan percaya diri saat melakukan refactoring. Awalnya terasa melelahkan karena kode test bisa lebih panjang dari kode implementasi aslinya. Namun, ketika saya mengubah logika di `Repository` (misalnya mengganti loop manual dengan `removeIf`), unit test langsung memberitahu jika ada yang salah (*regression testing*). Ini sangat membantu menjaga kualitas kode.

**2. Berapa banyak unit test yang cukup?**  
Tidak ada angka pasti, namun prinsipnya adalah setiap *branch* logika (if/else), *loop*, dan *exception handling* harus ter-cover. Namun, perlu diingat bahwa **100% Code Coverage tidak menjamin 100% Bug-Free**.
- Coverage hanya mengukur baris kode yang dieksekusi, bukan kebenaran logika bisnis.
- Contoh: Kode saya mungkin 100% covered, tapi jika saya lupa menangani logika "Stok barang tidak boleh negatif", test akan tetap hijau (passed) meskipun secara bisnis itu adalah bug.
- Oleh karena itu, kualitas test (menguji edge cases & boundary values) lebih penting daripada sekadar angka coverage.

**3. Kebersihan kode dalam Functional Test**  
Ketika membuat functional test baru untuk fitur lain, saya menyadari kode test saya menjadi tidak bersih karena banyak duplikasi (copy-paste) dari test sebelumnya.
- **Masalah:** Setup server, inisialisasi driver, dan definisi URL dasar diulang-ulang.
- **Solusi:** Seharusnya saya membuat **Base Test Class** yang menangani setup (`@BeforeEach`) dan teardown driver. Test class lain cukup melakukan *extends* ke kelas base tersebut. Ini akan membuat kode test lebih bersih dan mudah dipelihara.

---

## Module 2: CI/CD, Code Quality, and Deployment

### 1. Code Quality Issue(s) Fixed & Strategy

Selama exercise Module 2, saya menemukan beberapa *code quality issues* dari hasil analisis (SonarCloud) dan memperbaikinya dengan strategi **minimal changes, maximum impact**—yaitu memperbaiki isu yang jelas berdampak pada kualitas dan maintainability tanpa mengubah perilaku aplikasi.

#### A. Empty Method / Empty Test Body
Beberapa method pada test terdeteksi sebagai “kosong” atau tidak memiliki aksi yang jelas (misalnya `setUp()` yang kosong, atau test yang hanya menjalankan kode tanpa verifikasi). Hal ini menurunkan kualitas test karena tidak ada bukti bahwa perilaku benar-benar diverifikasi.

**Strategi perbaikan:**
- Untuk lifecycle method seperti `setUp()`, saya memastikan method ini benar-benar melakukan inisialisasi yang diperlukan (misalnya membuat instance repository untuk tiap test agar test terisolasi).
- Untuk test case yang hanya menjalankan method tanpa verifikasi, saya menambahkan assertion seperti `assertDoesNotThrow(...)` atau `assertNotNull(...)` agar test memiliki tujuan yang jelas.

#### B. Missing Assertion in Test Case
Sonar menandai test yang tidak memiliki assertion karena test semacam ini bisa “selalu hijau” walau sebenarnya tidak memverifikasi apa pun.

**Strategi perbaikan:**
- Saya menambahkan assertion yang relevan dan aman, misalnya `assertDoesNotThrow` untuk memastikan aplikasi dapat dijalankan tanpa exception dalam mode non-web.
- Untuk test Spring context, saya menggunakan injection `ApplicationContext` lalu melakukan `assertNotNull(context)` untuk membuktikan context benar-benar terbentuk.

#### C. Unused Imports / Minor Cleanliness Issues
Ada import yang tidak digunakan pada beberapa test class (contohnya pada test controller). Meski bukan bug, ini mengganggu readability dan sering dianggap “noise” pada code review.

**Strategi perbaikan:**
- Saya membersihkan import yang tidak terpakai agar kode lebih rapi dan sesuai standar clean code.
- Saya memisahkan tiap fix ke commit yang independen agar histori perubahan mudah ditinjau dan sesuai instruksi modul.

---

### 2. Reflection on CI/CD Workflows: Does It Meet CI & CD?

Menurut saya, workflow yang saya implementasikan **sudah memenuhi definisi Continuous Integration (CI)** karena setiap ada `push` atau `pull_request`, pipeline akan otomatis menjalankan proses build dan testing (termasuk JaCoCo dan analisis kualitas kode). Dengan demikian, integrasi perubahan dari berbagai branch selalu divalidasi secara otomatis, sehingga masalah dapat terdeteksi lebih awal sebelum merge ke branch utama.

Untuk **Continuous Deployment (CD)**, implementasi saya juga **mendekati/termasuk CD** karena setelah perubahan masuk ke `main/master`, workflow deploy akan berjalan otomatis dan menerapkan versi terbaru ke environment PaaS tanpa proses manual developer. Ini berarti rilis ke environment deployment terjadi secara kontinu berbasis event (push/merge) dan bukan berdasarkan langkah manual developer.

Namun, kualitas CD sangat bergantung pada aturan trigger yang dipakai. Jika deploy diatur untuk berjalan **hanya ketika workflow CI sukses**, maka alurnya lebih ideal karena memastikan aplikasi yang dideploy sudah lolos pengujian dan quality gate. Dengan demikian, pipeline saya bukan hanya otomatis, tetapi juga konsisten menjaga kualitas sebelum deployment dilakukan.

---

## Module 3: Maintainability & SOLID Principles

### 1) Principles Applied to the Project

Pada tahap `after-solid`, saya menerapkan prinsip-prinsip SOLID berikut pada project Eshop:

- **SRP (Single Responsibility Principle)**  
  Saya memisahkan controller berdasarkan domain/fitur agar masing-masing class punya tanggung jawab yang jelas. Contoh: `ProductController` fokus pada fitur Product, sedangkan `CarController` berada di file/class terpisah untuk fitur Car. Ini mengurangi “campur aduk” logic dan memudahkan perubahan per fitur.

- **OCP (Open-Closed Principle)**  
  Saya membuat titik ekstensi agar perubahan aturan tidak memaksa perubahan kode lama. Contoh: pembuatan ID mobil dipisahkan ke abstraksi `IdGenerator`. Jika nanti cara generate ID berubah (misalnya dari UUID ke format lain), saya cukup menambah implementasi baru tanpa mengubah logic repository yang sudah stabil.

- **LSP (Liskov Substitution Principle)**  
  Saya menghindari inheritance yang tidak merepresentasikan relasi “is-a”. Contoh: `CarController` tidak lagi mewarisi `ProductController` karena controller untuk Car bukanlah “jenis dari” controller Product. Saya mengganti inheritance menjadi komposisi (controller punya dependency service yang dibutuhkan).

- **ISP (Interface Segregation Principle)**  
  Interface `CarService` dibuat spesifik dan tidak terlalu besar. Method yang ada benar-benar relevan untuk operasi Car (create, read, update, delete). Dengan ini, class yang memakai interface tidak dipaksa mengetahui method yang tidak dibutuhkan.

- **DIP (Dependency Inversion Principle)**  
  Layer atas (Controller) bergantung pada abstraksi (interface) bukan implementasi. Contoh: controller bergantung pada `CarService` (interface), bukan `CarServiceImpl`. Selain itu, dependency digunakan lewat *constructor injection* agar lebih mudah di-test dan lebih longgar coupling-nya.

---

### 2) Advantages of Applying SOLID (with Examples)

Menerapkan SOLID memberikan keuntungan nyata pada maintainability, testability, dan extensibility:

- **Maintainability meningkat (perubahan lebih lokal dan aman)**  
  Dengan SRP, perubahan pada fitur Car tidak mengganggu Product, karena file/class sudah terpisah. Misalnya, jika saya mengubah alur `edit car`, saya hanya perlu fokus pada `CarController` dan `CarService`, tidak perlu menyentuh `ProductController`.

- **Testing lebih mudah dan coverage meningkat**  
  Dengan DIP (inject interface + constructor injection), saya bisa membuat unit test menggunakan mock untuk dependency. Contoh: `CarServiceImplTest` dapat mem-mock `CarRepository` dan hanya menguji logic service tanpa harus memanggil dependency nyata.

- **Extensibility tanpa banyak modifikasi (OCP)**  
  Dengan `IdGenerator`, saya bisa menambah strategi ID baru tanpa mengubah `CarRepository`. Contohnya, jika nanti saya ingin ID seperti `CAR-2026-0001`, saya cukup membuat `CarSequentialIdGenerator` (implementasi baru) lalu mengganti bean yang dipakai, tanpa mengubah logika CRUD.

- **Mengurangi risiko desain “aneh” dari inheritance (LSP)**  
  Menghapus inheritance `CarController` → `ProductController` mencegah hubungan yang tidak tepat dan menghindari side effect (misalnya mapping atau behavior yang tidak sengaja “ikut” karena pewarisan). Desain menjadi lebih jelas: controller berkorespondensi dengan fitur.

---

### 3) Disadvantages of NOT Applying SOLID (with Examples)

Jika SOLID tidak diterapkan, dampaknya pada project biasanya terasa saat fitur bertambah:

- **Kode cepat menjadi “god class” dan sulit dirawat (melanggar SRP)**  
  Jika `ProductController` juga mengelola Car (dua controller dalam satu file/class), maka file menjadi semakin panjang. Perubahan kecil pada Car berpotensi merusak Product karena lokasi logic bercampur dan sulit dilacak.

- **Sulit di-test dan banyak perubahan memicu ripple effect (melanggar DIP)**  
  Jika controller bergantung langsung pada `CarServiceImpl` atau membuat object sendiri (`new`), unit test akan sulit karena Anda harus membawa implementasi asli. Akibatnya, testing jadi berat dan perubahan implementasi mudah merusak banyak test.

- **Perubahan requirement memaksa modifikasi di banyak tempat (melanggar OCP)**  
  Jika logika generate ID ada langsung di repository, maka setiap perubahan aturan ID akan membuat repository harus diubah dan diuji ulang. Ini meningkatkan risiko bug dan memperpanjang waktu development.

- **Inheritance yang tidak tepat membuat perilaku tidak konsisten (melanggar LSP)**  
  Misalnya `CarController extends ProductController`: secara desain, car bukan substitusi product controller. Ini dapat membuat developer bingung dan menimbulkan bug karena asumsi yang tidak valid tentang perilaku class turunan.