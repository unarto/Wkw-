Buat project Android baru bernama WKW Xplore.

IDENTITAS
Nama aplikasi: WKW Xplore
Package/Namespace: com.wakwau.xplore
Root package: com.wakwau.xplore

TUJUAN TAHAP 1
Bangun fondasi File Manager dual-panel yang modular, ringan dipelihara, dan siap menerima feature module pada tahap berikutnya.

JANGAN mengimplementasikan fitur di luar File Manager pada tahap ini.

GRADLE MODULE

:app
:core
:core-ui
:core-storage
:filemanager
:filemanager-ui
:treeview

DEPENDENCY

:app
 └── :filemanager-ui

:filemanager-ui
 ├── :filemanager
 ├── :core-ui
 └── :treeview

:filemanager
 ├── :core
 └── :core-storage

:core-storage
 └── :core

DILARANG membuat:
:filemanager -> :app
:filemanager-ui -> :app
circular dependency
duplicate implementation

ARSITEKTUR

:core
→ shared abstraction, utility, result/error, coroutine infrastructure.

:core-ui
→ reusable Compose UI dan komponen UI umum.

:core-storage
→ abstraction storage.
→ StorageProvider.
→ StorageLocation.
→ FileEntry.
→ FileMetadata.
→ StorageCapabilities.
→ StorageException.
→ operasi storage yang bersifat generic.

:filemanager
→ seluruh domain dan logic File Manager.
→ repository.
→ use case.
→ operation.
→ search.
→ navigation.
→ state.
→ ViewModel.

:filemanager-ui
→ seluruh UI File Manager.
→ tidak boleh berisi business logic atau filesystem implementation.

:treeview
→ reusable TreeView component.
→ TreeNode.
→ TreeState.
→ TreeController.
→ jangan coupling ke File Manager secara langsung jika tidak diperlukan.

FILE MANAGER

Implementasikan:

- dual panel
- panel kiri
- panel kanan
- active panel
- navigasi folder
- breadcrumb
- back
- forward
- history
- bookmark
- recent locations
- list view
- grid view
- sorting
- multi-selection
- copy
- move
- rename
- delete
- create folder
- create file
- file properties
- file preview
- recursive search
- content search
- Find & Replace recursive dalam satu folder
- operation progress
- cancellation
- conflict handling
- TreeView
- location/storage picker

DUAL PANEL

Setiap panel harus mempunyai state sendiri.

Minimal:

PanelState
DualPanelState
SelectionState
ViewMode
SortMode
NavigationState

Dukung:

Left Panel
Right Panel
Active Panel

Operasi copy/move dapat menggunakan:

source = panel aktif
destination = panel lainnya

Tetapi jangan mengunci architecture hanya pada skenario tersebut.

SRP / SINGLE RESPONSIBILITY

JANGAN membuat God Class.

JANGAN membuat satu FileOperationManager berisi seluruh implementasi operasi.

Pisahkan operasi secara independen:

Copy.kt
Move.kt
Delete.kt
Rename.kt
CreateFolder.kt
CreateFile.kt
FileProperties.kt
CalculateSize.kt
Search.kt
FindReplace.kt

Setiap class hanya mempunyai satu tanggung jawab.

Contoh:

Copy.kt
→ hanya copy.

Move.kt
→ hanya move.

Delete.kt
→ hanya delete.

Rename.kt
→ hanya rename.

Search.kt
→ hanya search.

FindReplace.kt
→ hanya find & replace.

Jika diperlukan UseCase, gunakan:

CopyUseCase.kt
MoveUseCase.kt
DeleteUseCase.kt
RenameUseCase.kt
CreateFolderUseCase.kt
SearchUseCase.kt
FindReplaceUseCase.kt

UseCase hanya mengorkestrasi dependency.
Implementation operasi tetap berada pada class operasi masing-masing.

BACKGROUND OPERATION

Semua operasi berat WAJIB berjalan asynchronous/background.

Termasuk:

- copy file besar
- move file besar
- delete recursive
- copy banyak file
- move banyak file
- recursive search
- content search
- Find & Replace recursive
- menghitung ukuran folder
- operasi directory recursive

JANGAN melakukan operasi filesystem berat di Main thread.

Gunakan coroutine dan dispatcher yang sesuai.

UI harus tetap responsif selama operasi berjalan.

Setiap operasi harus mendukung:

- progress
- current file
- processed count
- total count jika tersedia
- status
- success
- failure
- error
- cancellation

Cancellation harus benar-benar diteruskan ke operasi filesystem dan tidak hanya menyembunyikan progress UI.

Hindari coroutine leak.

Pastikan lifecycle ViewModel aman.

ARCHITECTURE OPERASI

Gunakan pola:

UI
↓
ViewModel
↓
UseCase
↓
Operation
↓
StorageProvider
↓
Storage implementation

Contoh:

CopyUseCase
↓
Copy
↓
StorageProvider

MoveUseCase
↓
Move
↓
StorageProvider

DeleteUseCase
↓
Delete
↓
StorageProvider

SearchUseCase
↓
Search
↓
StorageProvider

FindReplaceUseCase
↓
FindReplace
↓
StorageProvider

STORAGE ABSTRACTION

Jangan membuat File Manager bergantung langsung pada filesystem implementation tertentu.

Gunakan interface seperti:

StorageProvider

StorageProvider harus menjadi abstraction yang memungkinkan backend storage ditambahkan kemudian tanpa mengubah domain/UI File Manager.

Jangan implementasikan backend tambahan pada tahap ini.

SEARCH

Search harus dipisahkan dari UI.

Pisahkan:

Filename Search
Content Search
Recursive Search
Find & Replace

Find & Replace harus dapat bekerja recursive terhadap satu folder.

Jangan membaca seluruh file besar ke memory jika tidak diperlukan.

Gunakan streaming/chunked processing untuk file besar jika architecture memungkinkan.

RESOURCE / PACKAGE

Gunakan:

com.wakwau.xplore.core
com.wakwau.xplore.core.ui
com.wakwau.xplore.storage
com.wakwau.xplore.filemanager
com.wakwau.xplore.filemanager.ui
com.wakwau.xplore.treeview

Struktur package harus konsisten.

Jangan mengubah package menjadi package lain.

UI

Gunakan UI technology yang dipakai project.

Jangan membuat perubahan visual yang tidak diperlukan.

Fokus pada struktur dan behavior File Manager.

UI harus tetap terpisah dari:

filesystem
storage implementation
file operation
search engine
business logic

RESOURCE OWNERSHIP

Resource yang hanya digunakan module tertentu harus berada di module tersebut.

Jangan menduplikasi resource.

Jangan membuat resource global jika hanya digunakan satu module.

GRADLE

Buat:

settings.gradle.kts
build.gradle.kts
module/build.gradle.kts

untuk seluruh module Tahap 1.

Gunakan versi AGP/Kotlin/Compose/SDK yang konsisten dengan konfigurasi project yang dibuat.

Jangan mengubah versi dependency secara sembarangan.

Konfigurasi setiap module hanya dengan dependency yang benar-benar digunakan.

DESUGARING

Jika module membutuhkan core library desugaring, konfigurasi pada module yang membutuhkan.

Jangan mengandalkan konfigurasi :app secara tidak sengaja.

Hanya tambahkan desugaring jika memang diperlukan.

TESTING

Tambahkan test untuk logic penting, terutama:

- copy
- move
- delete
- rename
- recursive search
- Find & Replace
- cancellation
- conflict handling
- dual-panel state

Jangan membuat test palsu hanya untuk membuat build hijau.

VALIDASI

Setelah implementasi:

1. Gradle sync.
2. Debug build.
3. Jalankan unit test.
4. Perbaiki compile error.
5. Periksa dependency graph.
6. Periksa circular dependency.
7. Periksa duplicate class.
8. Periksa resource collision.
9. Pastikan tidak ada filesystem operation berat di Main thread.
10. Pastikan :app dapat membuka File Manager.
11. Pastikan dual-panel dapat berpindah active panel.
12. Pastikan operasi file berjalan asynchronous.
13. Pastikan progress dan cancellation bekerja.

ATURAN KERAS

Jangan membuat God Class.

Jangan membuat duplicate implementation.

Jangan membuat dependency ke :app.

Jangan memindahkan class hanya untuk menghilangkan compile error.

Jangan membuat stub fitur yang belum diperlukan.

Jangan mengimplementasikan feature module tahap berikutnya.

Jangan mengurangi fitur File Manager yang diminta.

Jangan mengubah UI tanpa alasan.

Jangan memasukkan seluruh dependency :app ke setiap module.

Gunakan SRP dan separation of concerns secara nyata.

HASIL AKHIR

Setelah selesai tampilkan laporan singkat:

1. Struktur module final.
2. Dependency graph.
3. Struktur package File Manager.
4. Daftar class operasi.
5. Daftar operasi yang berjalan di background.
6. Storage abstraction.
7. Test yang dibuat.
8. Hasil Gradle build.
9. Error yang ditemukan dan perbaikannya.

BERHENTI SETELAH TAHAP 1 SELESAI.
