# PROGRESS - WKW Xplore

Project: WKW Xplore (Dual-Panel Modular File Manager)
Package: com.wakwau.xplore
Current Stage: TAHAP 7B - Architecture Guard / Decoupling Handler (COMPLETED & VERIFIED)

---

## 🔄 SEDANG DIKERJAKAN
- *Tidak ada.*

---

## 📋 BELUM DIKERJAKAN
- [ ] Tahap berikutnya: Implementasi Compose UI

---

## ✅ SELESAI
- [x] Inisialisasi metadata project `metadata.json` (Nama: WKW Xplore)
- [x] Konfigurasi multi-module `settings.gradle.kts` (`:app`, `:core`, `:core-ui`, `:core-storage`, `:filemanager`, `:filemanager-ui`, `:treeview`)
- [x] Konfigurasi Gradle version catalog `libs.versions.toml` & top-level `build.gradle.kts`
- [x] Konfigurasi custom launcher icon & background (WKW Xplore branding)
- [x] Implementasi modul `:core` (Result/Error pattern, Dispatchers abstraction, ByteFormatter, DateFormatter, MimeTypeDetector)
- [x] Implementasi modul `:core-storage` (FileEntry, FileMetadata, StorageLocation, StorageCapabilities, StorageException, StorageProvider interface, LocalStorageProvider)
- [x] Refactor & Integrasi modul resmi `:treeview` (`com.wakwau.xplore.treeview`)
  - [x] Konfigurasi Jetpack Compose pada build module `:treeview`.
  - [x] Semua implementasi *Tree View* dipindahkan ke Root modul `treeview/`.
  - [x] Setiap komponen UI (*class/function*) dipisah ke file *class.kt* masing-masing secara spesifik berorientasi SRP (`TreeBranchGuide.kt`, `TreeExpandToggle.kt`, `AndroidTreeViewComposable.kt`, `FileNodeViewHolder.kt`).
- [x] Implementasi modul `:core-ui` (Theme, Material 3 Color scheme, Typography, BreadcrumbBar, StorageDiskBar, FileIcon, Dialogs)
- [x] Audit & Refactor 10 Controller/Manager Kelas berdasarkan ketergantungan dan tanggung jawab (Clean Architecture Multi-Module):
  - `:filemanager` (Pure Domain Module - Bebas dari UI & Lifecycle Android):
    - `com.wakwau.xplore.filemanager.directory.DirectoryLoader`
    - `com.wakwau.xplore.filemanager.navigation.DirectoryNavigationManager`
    - `com.wakwau.xplore.filemanager.state.FileManagerStateHolder`
    - `com.wakwau.xplore.filemanager.state.PanelSelectionManager`
    - `com.wakwau.xplore.filemanager.tree.FolderExpansionManager`
    - `com.wakwau.xplore.filemanager.operation.OperationsController`
    - `com.wakwau.xplore.filemanager.operation.SearchController`
  - `:filemanager-ui` (UI & Lifecycle Orchestration Module):
    - `com.wakwau.xplore.filemanager.viewmodel.FileManagerViewModel`
    - `com.wakwau.xplore.filemanager.viewmodel.FileManagerViewModelFactory`
    - `com.wakwau.xplore.filemanager.panel.PanelDisplayManager`
- [x] Hapus paket `viewmodel` dan dependency `androidx.lifecycle` dari modul `:filemanager` untuk menjamin pure domain logic.
- [x] Refactor unit test (`DualPanelStateTest.kt`) ke modul `:filemanager-ui`.
- [x] Validasi penuh build (`compile_applet`) — BUILD SUCCEEDED (PASS)
- [x] Modularisasi SRP Modul `:filemanager-ui` (Pure UI Layer per `dokumentasi.md` baris 74-76):
  - `com.wakwau.xplore.filemanager.ui`: `FileManagerScreen`, `FileManagerTopBar`, `DualPanelLayout`
  - `com.wakwau.xplore.filemanager.ui.panel`: `PanelContainer`, `FileList`, `FileGrid`, `FileListItem`, `ActionSidebar`
  - `com.wakwau.xplore.filemanager.ui.dialogs`: `FileManagerDialogHost`, `CreateFolderDialog`, `CreateFileDialog`, `RenameDialog`, `DeleteConfirmDialog`, `FilePreviewDialog`, `PropertiesDialog`, `SearchDialog`, `FindReplaceDialog`, `BookmarksDialog`, `StoragePickerDialog`, `ConflictResolutionDialog`, `OperationProgressDialog`
- [x] Cleanup Phase: Hapus seluruh referensi obsolete `:filemanager`, `:filemanager-ui`, `com.wakwau.xplore.filemanager`, `com.wakwau.xplore.filemanager.ui` dari `settings.gradle.kts`, `app/build.gradle.kts`, dan `MainActivity.kt`.
- [x] Refactor `MainActivity.kt` sebagai murni Entry Point yang memanggil `XploreRoot()` composable dengan `WKWXploreTheme`.
- [x] Update `XploreRoot.kt` menampilkan text "WKW Xplore".
- [x] **TAHAP 7B - Perbaikan Coupling Handler (Selesai)**
  - [x] Snapshot `DualPaneState` dicapture oleh ViewModel *sebelum* `coroutine launch`.
  - [x] Seluruh handler (`CopyOperationHandler`, `MoveOperationHandler`, dkk) HANYA menerima parameter pure (seperti `DualPaneState`, `PanelId`, `String`), menghapuskan ketergantungan pada `MutableStateFlow` dan `viewModelScope`.
  - [x] Lifecycle (CoroutineScope) dipegang 100% secara eksklusif oleh ViewModel.
  - [x] BUILD PASS (`:filemanager-ui:compileDebugKotlin` / `testDebugUnitTest`) tanpa error.

---

## ⏸️ TERTUNDA
- [ ] Implementasi `CreateFileUseCase` (Masih throw `NOT_SUPPORTED`). Perbaikan sengaja tertunda untuk mencegah pelebaran scope di luar batas arsitektur per tahap 7B.

---

## 🔍 DITEMUKAN
- [ ] **Hardcoded Dispatchers.IO**: Ditemukan injeksi statis dispatcher I/O pada `core-storage` (Backend & Repository) yang idealnya di-_inject_ melalui abstrasi `AppDispatchers`. Ini dicatat sebagai *technical debt* yang dibiarkan pada fase ini.

---

## ❌ DIBATALKAN
- *Tidak ada.*
