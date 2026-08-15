# PHASE 1 UI AUDIT

## 1. Existing UI Source Map

| File | Class | Responsibility | Dependencies |
|------|-------|----------------|--------------|
| `core-ui/.../AppDialog.kt` | `AppDialog` | Reusable generic dialog container | Compose UI, Material3 |
| `core-ui/.../StorageDiskBar.kt` | `StorageDiskBar` | Visual rendering of storage disk info (free space, usage bar) | Compose UI, Material3 |
| `core-ui/.../BreadcrumbBar.kt` | `BreadcrumbBar` | Visual rendering of horizontal scrolling breadcrumb items | Compose UI, Material3 |
| `core-ui/.../FileIcon.kt` | `FileIcon` | Mapping of `FileCategory` to vector icons / colors | Compose UI, Material Icons |
| `app/.../XploreRoot.kt` | `XploreRoot` | Temporary placeholder entry point showing "WKW Xplore" text | Compose UI |
| `filemanager-ui/.../DualPaneViewModel.kt` | `DualPaneViewModel` | Screen-level State Holder & Event Orchestrator | UI State, UI Event, Action Handlers |
| `filemanager-ui/.../DualPaneReducer.kt` | `DualPaneReducer` | Pure State Transformation (`State + Event -> State`) | UI State, UI Event |
| `filemanager-ui/.../DualPaneState.kt` | `DualPaneState` | Immutable UI State representation for dual-pane file manager | `core-storage-api` models |

## 2. Existing Compose Components
- `AppDialog` (in `:core-ui`)
- `StorageDiskBar` (in `:core-ui`)
- `BreadcrumbBar` (in `:core-ui`)
- `FileIcon` (in `:core-ui`)
- `ComposeTreeView` (in `:treeview`)
- `TreeBranchGuide` (in `:treeview`)
- `TreeExpandToggle` (in `:treeview`)
- `TreeNodeRow` (in `:treeview`)

## 3. Existing Screen Architecture
Belum ada arsitektur *Screen* yang terimplementasi secara penuh untuk File Manager. Titik entri aplikasi berada di `MainActivity` yang langsung memanggil `XploreRoot` (sebagai dummy/placeholder). Struktur direktori `screen/`, `component/`, dan `navigation/` di dalam `:filemanager-ui` belum dibuat.

## 4. Existing State Architecture
Arsitektur State sudah sangat kokoh, terpusat pada *Single Source of Truth* melalui `DualPaneState`.
- `DualPaneState` memiliki dua `PanelState` (kiri dan kanan).
- `PanelState` memuat daftar `FileItem`, `currentLocation`, state `isLoading`, `error`, dan `selectedItemIds`.
- `OperationUiState` memuat status *background operations*.
Semua class state merupakan tipe *immutable* (data class).

## 5. Existing Event Flow
Alur *Unidirectional Data Flow* (UDF) berjalan sempurna:
1. UI mengirim `DualPaneEvent` (seperti `OpenLocation`, `CopySelected`) ke `DualPaneViewModel.dispatch()`.
2. `DualPaneReducer` menghitung sinkronous state (misal mengubah `isLoading = true`).
3. State di-*emit* kembali ke UI.
4. `DualPaneViewModel` menangani *side-effects* (meluncurkan coroutine ke Handler yang sesuai).

## 6. Existing Operation Flow
Operasi dipisahkan secara murni (*SRP*) menjadi Handler yang bertindak sebagai *orchestrator side-effects*:
- `CopyOperationHandler`
- `MoveOperationHandler`
- `DeleteOperationHandler`
- `RenameOperationHandler`
- `CreateDirectoryOperationHandler`
- `PanelRefreshHandler`
- `PanelNavigationHandler`
Handler ini hanya memanggil `UseCase` dari `:filemanager` dan mengekspos hasilnya kembali ke UI Event.

## 7. Existing TreeView Integration
Modul `:treeview` sudah eksis dan 100% *generic*, tidak menyentuh filesystem I/O maupun entitas `FileItem`. 
Namun, saat ini **belum diintegrasikan** ke `filemanager-ui`. Perlu dibuat titik integrasi (seperti `FileTreeStateAdapter` atau `FileTreeView`) pada Tahap-tahap selanjutnya.

## 8. Screenshot -> Component Mapping

| SCREENSHOT ELEMENT | TARGET COMPOSABLE | DATA SOURCE | EVENT SOURCE |
|---|---|---|---|
| Top App Bar | `FileManagerTopBar` | App Metadata | Navigation / Overflow Click |
| Breadcrumb | `BreadcrumbBar` (dari `core-ui`) | `PanelState.currentLocation` | `BreadcrumbNavigation` |
| Storage Header | `StorageDiskBar` (dari `core-ui`) | `StorageLocation` / Metadata | Panel Click Event |
| Storage Usage | `StorageDiskBar` (progress) | Storage capacity data | N/A |
| File List | `FileList` | `PanelState.items` | List Scroll / Interactions |
| File Row | `FileListItem` | `FileItem` / `FileItemUiModel` | Item Click/Long Press |
| Folder/File Icon | `FileIcon` (dari `core-ui`) | `FileCategory` | N/A |
| Expand Toggle | `TreeExpandToggle` (dari `treeview`) | `TreeState` | `TreeInteraction` |
| Tree Branch | `TreeBranchGuide` (dari `treeview`) | `FlattenedTreeNode.depth` | N/A |
| Selection Indicator | `FileItemSelectionIndicator` | `PanelState.selectedItemIds` | Item Selection Click |
| Panel Divider | `PanelDivider` | Layout Split Config | Layout Resize |
| Side Action Bar | `SideActionBar` | Hardcoded/Configurable Action List | Side Action Click |
| Side Action | `SideActionItem` | `SideActionUiModel` | Action Click |
| Loading State | `LoadingContent` | `PanelState.isLoading` | N/A |
| Empty State | `EmptyContent` | `PanelState.items.isEmpty()` | N/A |
| Error State | `ErrorContent` | `PanelState.error` | N/A |
| Operation Status | `OperationProgressDialog` | `DualPaneState.operationState` | Cancel Click |

## 9. SRP Audit
Seluruh *Class* yang sudah ada (*existing*) patuh 100% terhadap Single Responsibility Principle. Reducer hanya me-*reduce* (*pure mapping*). Handler hanya mengorkestrasi side-effect. *Core UI Components* bersifat *stateless*.

## 10. God Class Candidates
**TIDAK ADA**. Semua struktur Backend dan Reducer mematuhi SRP. Pembuatan *God Class* berhasil dicegah.

## 11. Duplicate State Audit
**TIDAK ADA**. Saat ini UI layer baru berupa state management (`ViewModel`). Tidak ditemukan *duplicate local mutable state* (`remember { mutableStateOf(...) }`) untuk logika *file selection* maupun manajemen status panel, karena composable belum dibentuk.

## 12. Dependency Leakage
**TIDAK ADA**. `:filemanager-ui` menggunakan `UseCase` dan domain model (`:core-storage-api`), tidak menyentuh *Repository* atau kelas `java.io.File` secara langsung.

## 13. Filesystem Leakage
**TIDAK ADA**. Komponen UI dan modul presentasi bersih dari referensi filesystem konkret.

## 14. TreeView Boundary Audit
Modul `:treeview` 100% *generic*, tersolasi pada payload `<T>`. Ia buta terhadap *business logic* dari File Manager, dan dapat digunakan di sembarang tempat.

## 15. Target UI Class Map
Struktur direkomendasikan untuk diimplementasi di dalam `:filemanager-ui` (mengikuti arahan dari `ui.md`):
- `screen/DualPaneFileManagerScreen.kt`
- `screen/FileManagerContent.kt`
- `component/FileManagerPanel.kt`
- `component/FileManagerTopBar.kt`
- `component/SideActionBar.kt`
- `component/FileList.kt`
- `component/FileListItem.kt`
- `tree/FileTreeView.kt`
- `tree/FileTreeNodeMapper.kt`
- `model/FileItemUiModel.kt`
- `model/SideActionUiModel.kt`
- `mapper/FileItemUiMapper.kt`

## 16. Recommended Implementation Order
Berdasarkan audit kondisi aktual, eksekusi disarankan menggunakan urutan berikut:
1. **PHASE 2**: Buat `model` dan `mapper` untuk pemisahan domain dan `FileItemUiModel` di `:filemanager-ui`.
2. **PHASE 3**: Buat perancah UI tingkat atas (Scaffold, `FileManagerTopBar`, dan panggil komponen `BreadcrumbBar` & `StorageDiskBar` eksisting).
3. **PHASE 4**: Buat UI `FileManagerPanel` dan container *Dual Panel* (`FileManagerContent`, `DualPaneFileManagerScreen`).
4. **PHASE 5**: Implementasikan `FileList` dan komponen-komponen isinya (`FileListItem`).
5. **PHASE 6**: Buat adapter `FileTreeView` yang menjembatani payload FileItem ke `:treeview`.
6. **PHASE 7**: Bangun `SideActionBar`.
7. **PHASE 8**: Susun Dialog Operasi (`OperationProgressDialog`, `RenameDialog`).
8. **PHASE 9**: Ikat composable ke dalam `DualPaneViewModel` di `MainActivity`.

## 17. Files Requiring Change
- **MUST CHANGE**: 
  - `app/.../XploreRoot.kt` (nanti digantikan/diarahkan ke `DualPaneFileManagerScreen`).
- **MAY CHANGE**: 
  - `DualPaneEvent` jika membutuhkan tambahan intens spesifik UI (misalnya toggle TreeView, click side action).
- **NO CHANGE**: 
  - Seluruh modul `:core-storage`, `:filemanager`, `DualPaneReducer`, `OperationHandlers`, dan modul `:treeview`.

## 18. Architecture Verdict
**PASS**
Arsitektur layer berada dalam keadaan sempurna, terisolasi, dan sangat siap menerima pondasi *Jetpack Compose UI* yang ketat.
