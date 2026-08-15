============================================================
PHASE: FILE MANAGER UI IMPLEMENTATION
PROJECT: X-plore style Android File Manager
ARCHITECTURE GUARD: STRICT
============================================================

TUJUAN

Implementasikan UI File Manager berdasarkan screenshot X-plore yang diberikan.

Screenshot adalah VISUAL REFERENCE / VISUAL CONTRACT.

Target UI utama:

1. Dual-pane file manager.
2. Panel kiri dan kanan.
3. Header/top app bar.
4. Breadcrumb/path bar.
5. Storage location header.
6. File/folder list.
7. TreeView untuk hierarchy.
8. Side action toolbar.
9. Folder/file icons.
10. Selection state.
11. Expand/collapse tree.
12. Loading state.
13. Empty state.
14. Error state.
15. Operation state.
16. Responsive layout untuk portrait/landscape.
17. UI tetap menggunakan architecture yang sudah ada.

JANGAN membuat UI monolitik.

============================================================
ABSOLUTE ARCHITECTURE RULES
============================================================

RULE 1
JANGAN membuat God Class.

Tidak boleh ada satu file seperti:

DualPaneScreen.kt
FileManagerScreen.kt
MainFileManager.kt
FileBrowser.kt

yang berisi seluruh:
- toolbar
- breadcrumb
- panel
- file list
- tree
- selection
- dialog
- action toolbar
- navigation
- operation
- filesystem logic
- state mutation

Semua harus dipisahkan berdasarkan SRP.

RULE 2
UI TIDAK BOLEH mengakses filesystem secara langsung.

DILARANG di composable:

java.io.File
DocumentFile
ContentResolver
StorageManager
Uri parsing untuk operasi filesystem
FileRepository
StorageRepository
CopyFileUseCase
MoveFileUseCase
DeleteFileUseCase

UI hanya menerima UI state dan mengirim UI event.

RULE 3
JANGAN memasukkan domain FileItem ke module :treeview.

:treeview HARUS tetap generic.

TreeView hanya mengetahui:

TreeNode<T>

dan T adalah payload generic.

Mapping:

FileItem -> TreeNode<FileItem>

HARUS dilakukan di :filemanager-ui.

RULE 4
JANGAN membuat ViewModel di setiap composable.

Hanya screen-level state holder yang boleh berupa ViewModel.

Reusable composable harus stateless jika memungkinkan.

RULE 5
JANGAN membuat singleton untuk UI state.

RULE 6
JANGAN membuat global mutable state.

RULE 7
JANGAN memasukkan coroutine ke reusable visual component kecuali benar-benar diperlukan untuk UI behavior lokal.

RULE 8
JANGAN membuat repository baru hanya untuk membuat arsitektur terlihat lebih kompleks.

Gunakan backend yang sudah ada.

RULE 9
JANGAN memindahkan business logic ke UI.

RULE 10
JANGAN mengubah backend yang sudah ada kecuali compile error nyata yang disebabkan implementasi UI.

============================================================
EXISTING BACKEND / STATE CONTRACT
============================================================

Pertahankan class yang sudah ada:

DualPaneViewModel
DualPaneReducer
DualPaneState
PanelState
PanelId
OperationUiState
DualPaneEvent

Pertahankan operation handler:

CopyOperationHandler
MoveOperationHandler
DeleteOperationHandler
RenameOperationHandler
CreateDirectoryOperationHandler
PanelNavigationHandler
PanelRefreshHandler

Jangan mengganti architecture tersebut dengan implementasi baru.

DualPaneViewModel adalah screen-level state holder.

Reducer hanya pure state transformation.

Handler menangani orchestration / side effects.

Composable hanya render state dan mengirim event.

============================================================
UI MODULE CLASS MAP
============================================================

Gunakan struktur berikut.

:filemanager-ui/src/main/java/com/wakwau/xplore/filemanager/ui/

├── screen/
│   ├── DualPaneFileManagerScreen.kt
│   ├── FileManagerRoute.kt
│   └── FileManagerContent.kt
│
├── component/
│   ├── FileManagerTopBar.kt
│   ├── FileManagerPanel.kt
│   ├── FileManagerPanelHeader.kt
│   ├── FileManagerPanelContent.kt
│   ├── FileList.kt
│   ├── FileListItem.kt
│   ├── FileListDivider.kt
│   ├── FileItemIcon.kt
│   ├── FileItemName.kt
│   ├── FileItemMetadata.kt
│   ├── FileItemSelectionIndicator.kt
│   ├── BreadcrumbBar.kt
│   ├── BreadcrumbItem.kt
│   ├── StorageHeader.kt
│   ├── StorageUsageIndicator.kt
│   ├── PanelDivider.kt
│   ├── SideActionBar.kt
│   ├── SideActionItem.kt
│   ├── SelectionToolbar.kt
│   ├── LoadingContent.kt
│   ├── EmptyContent.kt
│   └── ErrorContent.kt
│
├── tree/
│   ├── FileTreeView.kt
│   ├── FileTreeNodeMapper.kt
│   ├── FileTreeNodeRow.kt
│   └── FileTreeStateAdapter.kt
│
├── navigation/
│   ├── FileManagerNavigation.kt
│   ├── PanelNavigation.kt
│   └── BreadcrumbNavigation.kt
│
├── dialog/
│   ├── FileOperationDialog.kt
│   ├── RenameDialog.kt
│   ├── CreateDirectoryDialog.kt
│   ├── DeleteConfirmationDialog.kt
│   └── OperationProgressDialog.kt
│
├── model/
│   ├── FileItemUiModel.kt
│   ├── BreadcrumbUiModel.kt
│   ├── StorageHeaderUiModel.kt
│   ├── SideActionUiModel.kt
│   └── FileManagerUiModel.kt
│
├── mapper/
│   ├── FileItemUiMapper.kt
│   ├── BreadcrumbUiMapper.kt
│   ├── StorageUiMapper.kt
│   └── PanelUiMapper.kt
│
├── state/
│   ├── DualPaneState.kt
│   ├── PanelState.kt
│   ├── OperationUiState.kt
│   └── UiStateExtensions.kt
│
├── event/
│   └── DualPaneEvent.kt
│
├── reducer/
│   └── DualPaneReducer.kt
│
├── action/
│   ├── CopyOperationHandler.kt
│   ├── MoveOperationHandler.kt
│   ├── DeleteOperationHandler.kt
│   ├── RenameOperationHandler.kt
│   ├── CreateDirectoryOperationHandler.kt
│   ├── PanelNavigationHandler.kt
│   └── PanelRefreshHandler.kt
│
└── presentation/
    └── DualPaneViewModel.kt

============================================================
RESPONSIBILITY MAP
============================================================

------------------------------------------------------------
DualPaneFileManagerScreen
------------------------------------------------------------

TANGGUNG JAWAB:

- screen entry point
- menerima ViewModel state
- meneruskan state ke FileManagerContent
- meneruskan event ke ViewModel

TIDAK BOLEH:

- filesystem logic
- copy/move/delete logic
- path manipulation
- tree construction logic
- dialog business logic
- operasi storage

Bentuk konseptual:

DualPaneFileManagerScreen(
    state,
    onEvent
)

------------------------------------------------------------
FileManagerRoute
------------------------------------------------------------

TANGGUNG JAWAB:

- menghubungkan ViewModel dengan screen
- collect StateFlow
- menyediakan event dispatcher

Tidak boleh berisi UI detail.

------------------------------------------------------------
FileManagerContent
------------------------------------------------------------

TANGGUNG JAWAB:

- menyusun layout utama
- top bar
- dual panel
- side action bar

Tidak boleh mengandung detail rendering item.

------------------------------------------------------------
FileManagerTopBar
------------------------------------------------------------

Hanya:

- back/up
- title
- top actions
- overflow

Tidak boleh:

- load directory
- copy file
- delete file
- manipulate repository

------------------------------------------------------------
FileManagerPanel
------------------------------------------------------------

TANGGUNG JAWAB:

Satu panel file manager.

Input:

PanelState

Output event:

DualPaneEvent

Isi:

- StorageHeader
- BreadcrumbBar
- FileManagerPanelContent

Tidak menangani backend.

------------------------------------------------------------
FileManagerPanelHeader
------------------------------------------------------------

Tanggung jawab:

- nama storage
- current path
- free/total storage
- storage icon

------------------------------------------------------------
StorageHeader
------------------------------------------------------------

Hanya render informasi storage.

Tidak menghitung storage.

Storage calculation berasal dari backend/state.

------------------------------------------------------------
StorageUsageIndicator
------------------------------------------------------------

Hanya visual progress indicator.

Input:

used
total
free

Tidak melakukan filesystem query.

------------------------------------------------------------
BreadcrumbBar
------------------------------------------------------------

Tanggung jawab:

- render breadcrumb
- menerima klik breadcrumb
- menghasilkan navigation event

Tidak melakukan navigation sendiri.

------------------------------------------------------------
BreadcrumbItem
------------------------------------------------------------

Hanya render satu breadcrumb item.

------------------------------------------------------------
FileList
------------------------------------------------------------

Tanggung jawab:

- render list
- lazy rendering
- selection visual
- click event forwarding

Tidak mengetahui cara mengambil file.

------------------------------------------------------------
FileListItem
------------------------------------------------------------

Hanya render SATU file/folder.

Bertanggung jawab terhadap:

- icon
- name
- metadata
- selection indicator
- expand indicator jika diperlukan

Tidak boleh mengetahui repository/usecase.

------------------------------------------------------------
FileItemIcon
------------------------------------------------------------

Hanya menentukan/render icon berdasarkan FileItemUiModel.

Jangan masukkan operasi filesystem.

------------------------------------------------------------
FileItemName
------------------------------------------------------------

Hanya text nama file.

------------------------------------------------------------
FileItemMetadata
------------------------------------------------------------

Hanya:

- size
- date
- child count
- secondary metadata

------------------------------------------------------------
FileItemSelectionIndicator
------------------------------------------------------------

Hanya visual selected/unselected.

------------------------------------------------------------
PanelDivider
------------------------------------------------------------

Hanya visual separator antara panel.

------------------------------------------------------------
SideActionBar
------------------------------------------------------------

Sidebar seperti screenshot X-plore.

Contoh action:

- TreeView
- Info
- Up
- Disk Map
- Rename
- Copy
- Paste
- ZIP
- Delete
- New Folder
- WiFi Server
- FTP
- New File
- Display

SideActionBar TIDAK menjalankan operasi.

Hanya emit event.

------------------------------------------------------------
SideActionItem
------------------------------------------------------------

Render satu tombol/action.

Input:

SideActionUiModel

------------------------------------------------------------
SelectionToolbar
------------------------------------------------------------

Muncul ketika ada selected items.

Hanya render selection actions.

Tidak menjalankan operation secara langsung.

------------------------------------------------------------
LoadingContent
------------------------------------------------------------

Hanya loading UI.

------------------------------------------------------------
EmptyContent
------------------------------------------------------------

Hanya empty state.

------------------------------------------------------------
ErrorContent
------------------------------------------------------------

Hanya error state.

Tidak melakukan recovery sendiri.

============================================================
TREEVIEW INTEGRATION
============================================================

JANGAN mengubah :treeview.

Existing generic TreeView:

TreeNode<T>
TreeState<T>
FlattenedTreeNode<T>
ComposeTreeView
TreeNodeRow
TreeBranchGuide
TreeExpandToggle
TreeInteraction

HARUS tetap generic.

File manager membuat adapter:

FileTreeNodeMapper

Tugas:

FileItem -> TreeNode<FileItem>

atau menggunakan FileItemUiModel sesuai kebutuhan UI.

Tidak boleh memasukkan FileItem ke source :treeview.

------------------------------------------------------------
FileTreeView
------------------------------------------------------------

Tanggung jawab:

- menjadi adapter UI file manager terhadap generic TreeView
- menerima tree data
- menghubungkan tree interaction dengan DualPaneEvent

Tidak memiliki filesystem logic.

------------------------------------------------------------
FileTreeNodeMapper
------------------------------------------------------------

Tanggung jawab:

mapping domain/UI file data menjadi TreeNode.

Contoh:

FileItem
    ↓
FileTreeNodeMapper
    ↓
TreeNode<FileItem>

Tidak melakukan I/O.

------------------------------------------------------------
FileTreeNodeRow
------------------------------------------------------------

Jika diperlukan, wrapper row khusus File Manager.

Tanggung jawab:

- icon folder/file
- name
- metadata
- indentation
- selection visual

TreeView generic tetap tidak tahu FileItem.

------------------------------------------------------------
FileTreeStateAdapter
------------------------------------------------------------

Tanggung jawab:

mengubah state generic TreeState menjadi state yang dibutuhkan File Manager UI.

Tidak melakukan filesystem access.

============================================================
NAVIGATION
============================================================

FileManagerNavigation:

Mengatur navigation level screen.

PanelNavigation:

Mengatur event navigation panel.

BreadcrumbNavigation:

Mengubah klik breadcrumb menjadi DualPaneEvent.

JANGAN membuat satu NavigationManager raksasa.

============================================================
DIALOG
============================================================

Setiap dialog harus single responsibility.

RenameDialog
    -> hanya input nama baru

CreateDirectoryDialog
    -> hanya input nama directory

DeleteConfirmationDialog
    -> hanya konfirmasi

FileOperationDialog
    -> hanya menampilkan status operation

OperationProgressDialog
    -> hanya progress/cancel UI

Dialog TIDAK BOLEH memanggil use case secara langsung.

Dialog hanya menghasilkan event.

============================================================
UI MODEL
============================================================

Buat UI model jika diperlukan untuk mencegah composable bergantung langsung
pada domain model.

FileItemUiModel:

- id
- name
- type
- icon
- size
- modifiedDate
- childCount
- isDirectory
- isSelected
- isExpandable

Jangan menaruh repository atau usecase ke UiModel.

============================================================
MAPPER RULE
============================================================

Mapper hanya melakukan transformasi:

Domain/Data
    ↓
UI Model

Mapper tidak boleh:

- melakukan I/O
- launch coroutine
- mengubah state
- memanggil usecase
- memanggil repository

============================================================
STATE RULE
============================================================

UI membaca immutable state.

UI tidak boleh melakukan:

state.selectedItemIds += ...
state.items.add(...)
state.currentLocation = ...

Semua perubahan:

UI event
    ↓
ViewModel
    ↓
Reducer / Handler
    ↓
new State
    ↓
UI

Gunakan UDF.

============================================================
DUAL PANEL CONTRACT
============================================================

Jangan membuat:

LeftPanelViewModel
RightPanelViewModel

kecuali benar-benar diperlukan.

Gunakan existing:

DualPaneState

yang memiliki:

leftPanel
rightPanel
activePanelId
operationState

Panel UI menerima PanelState.

Contoh konsep:

FileManagerPanel(
    panel = state.leftPanel,
    panelId = PanelId.LEFT,
    onEvent = onEvent
)

dan:

FileManagerPanel(
    panel = state.rightPanel,
    panelId = PanelId.RIGHT,
    onEvent = onEvent
)

============================================================
ACTIVE PANEL
============================================================

Active panel harus ditentukan dari:

state.activePanelId

UI tidak membuat source of truth sendiri.

============================================================
SELECTION
============================================================

Selection berasal dari:

PanelState.selectedItemIds

JANGAN membuat:

remember { mutableStateOf(...) }

sebagai duplicate selection state.

UI hanya menampilkan selection dan mengirim:

DualPaneEvent.SelectItem
DualPaneEvent.ToggleSelection
DualPaneEvent.ClearSelection

============================================================
FILE LIST CLICK
============================================================

Click event harus dipisahkan:

- select
- open
- toggle
- long click
- context action

Jangan membuat satu:

onClick()

yang berisi 50 kondisi.

Gunakan event yang sudah ada atau tambahkan event kecil
hanya jika memang diperlukan.

============================================================
TREE EXPANSION
============================================================

Tree expansion adalah UI/tree state.

Jangan memasukkan expansion logic ke FileRepository.

Generic TreeState<T> tetap bertanggung jawab terhadap:

- expand
- collapse
- toggle
- flatten
- visibleNodes

File Manager hanya menyediakan payload.

============================================================
VISUAL STRUCTURE
============================================================

Implementasikan berdasarkan screenshot X-plore.

Struktur visual:

TOP BAR
--------------------------------------------------
| Back | X-plore | actions | overflow |
--------------------------------------------------

BREADCRUMB
--------------------------------------------------
| Storage | Android | current folder |
--------------------------------------------------

STORAGE HEADER
--------------------------------------------------
| icon | storage name          free/total        |
|      | /storage/emulated/0    progress          |
--------------------------------------------------

MAIN AREA
--------------------------------------------------
|                |                               |
|    PANEL A     |          PANEL B              |
|                |                               |
|  folder list   |        folder list            |
|  file list     |        file list              |
|                |                               |
--------------------------------------------------

SIDE ACTION BAR

Jika mode portrait:

main content + vertical side toolbar

Jika landscape:

dua panel lebih lebar dan toolbar tetap terpisah.

============================================================
X-PLORE VISUAL CONTRACT
============================================================

Jangan membuat desain Material modern yang mengubah karakter screenshot.

Pertahankan karakter:

- dark background
- compact rows
- dense file manager layout
- folder icon
- file icon
- small metadata
- breadcrumb chips
- tree indentation
- thin separators
- side action toolbar
- compact top bar
- selected item indication
- expand/collapse indicator
- storage usage bar

Compose boleh digunakan, tetapi visual harus mengikuti screenshot.

============================================================
TREEVIEW VISUAL CONTRACT
============================================================

TreeView harus mendukung visual seperti screenshot:

- indentation berdasarkan depth
- connector/branch line
- expand/collapse indicator
- folder/file icon
- node name
- selected state
- compact row height

Gunakan existing:

TreeBranchGuide
TreeExpandToggle
TreeNodeRow
ComposeTreeView

Jangan membuat ulang TreeView implementation di FileManager.

============================================================
SRP SIZE GUARD
============================================================

Gunakan batas praktis:

Composable component:
ideal <= 80-120 LOC

State class:
ideal <= 100 LOC

Mapper:
ideal <= 100 LOC

Handler:
ideal <= 120 LOC

Jika class melewati batas tersebut:

STOP.

Evaluasi apakah tanggung jawabnya harus dipisahkan.

Jangan memecah class secara buta hanya berdasarkan jumlah baris.
Pisahkan berdasarkan responsibility.

============================================================
GOD CLASS DETECTION
============================================================

Class dianggap God Class jika melakukan 3 atau lebih
dari responsibility berikut:

1. rendering
2. navigation
3. state mutation
4. filesystem access
5. operation orchestration
6. dialog management
7. mapping
8. selection management
9. tree management
10. storage calculation
11. permission management
12. lifecycle management

Jika ditemukan:

JANGAN lanjut implementasi.

Refactor responsibility terlebih dahulu.

============================================================
DEPENDENCY DIRECTION
============================================================

Dependency yang DIIZINKAN:

screen
    ↓
presentation/state/event
    ↓
component

presentation
    ↓
action
    ↓
domain/usecase

tree adapter
    ↓
treeview

mapper
    ↓
domain model / UI model

Yang DILARANG:

treeview
    ↓
filemanager

component
    ↓
repository

component
    ↓
usecase

FileTreeView
    ↓
FileRepository

FileListItem
    ↓
CopyFileUseCase

============================================================
TREEVIEW BOUNDARY
============================================================

:treeview TIDAK BOLEH mengimpor:

com.wakwau.xplore.filemanager.*
com.wakwau.xplore.core.storage.*
FileItem
StorageLocation
FileRepository
CopyFileUseCase
MoveFileUseCase
DeleteFileUseCase

TreeView tetap generic.

============================================================
UI BOUNDARY
============================================================

:filemanager-ui boleh mengetahui:

FileItem
StorageLocation
DualPaneState
DualPaneEvent
domain UI contracts

Tetapi reusable visual components sebaiknya menerima
UI model/state yang diperlukan, bukan repository.

============================================================
NO DUPLICATE STATE
============================================================

Dilarang membuat duplicate state untuk:

selection
currentLocation
activePanel
loading
operation
items

Jika sudah tersedia di:

DualPaneState
PanelState
OperationUiState

gunakan state tersebut.

============================================================
NO BACKEND REWRITE
============================================================

Jangan membuat:

NewFileRepository
NewStorageRepository
NewFileOperationManager
NewFileManagerService
NewFileManagerEngine
NewFileManagerController

hanya demi UI.

Backend yang ada tetap menjadi backend.

============================================================
NO LEGACY TREEVIEW PORT
============================================================

JANGAN memindahkan AndroidTreeView.java secara mentah.

JANGAN membuat class seperti:

AndroidTreeViewManager
TreeViewController
TreeViewRepository
TreeViewService

Konsep legacy yang diperlukan harus dipetakan ke:

TreeNode<T>
TreeState<T>
ComposeTreeView
TreeInteraction

Legacy feature mapping:

expand/collapse
    -> TreeState

flatten
    -> TreeState

node relationship
    -> TreeNode

click
    -> TreeInteraction

rendering
    -> ComposeTreeView

selection file-manager
    -> FileManager UI state

filesystem loading
    -> FileManager backend

============================================================
FEATURE MAPPING
============================================================

Legacy TreeView:

TreeNode
    -> generic TreeNode<T>

expand
    -> TreeNode.expand()

collapse
    -> TreeNode.collapse()

toggle
    -> TreeNode.toggleExpanded()

children
    -> TreeNode.children

parent
    -> TreeNode.parent

depth
    -> TreeNode.depth

flatten
    -> TreeState.visibleNodes

selection
    -> FileManager selection state

save/restore expansion
    -> implement only if required by product
       and keep it outside generic TreeView if filesystem-specific

path resolution
    -> MUST NOT be placed inside generic TreeView

filesystem loading
    -> MUST NOT be placed inside generic TreeView

============================================================
IMPLEMENTATION ORDER
============================================================

Implement in this order:

PHASE 1
Audit existing UI files.

Do NOT modify anything yet.

Produce:

- current UI source map
- current dependency map
- current God Class candidates
- duplicate state candidates
- existing reusable components
- existing Compose theme components

PHASE 2

Create/refine UI models only where necessary.

PHASE 3

Implement:

FileManagerTopBar

BreadcrumbBar

StorageHeader

StorageUsageIndicator

PHASE 4

Implement:

FileManagerPanel

FileManagerPanelHeader

FileManagerPanelContent

PHASE 5

Implement:

FileList

FileListItem

FileItemIcon

FileItemName

FileItemMetadata

FileItemSelectionIndicator

PHASE 6

Integrate generic TreeView through:

FileTreeView
FileTreeNodeMapper
FileTreeNodeRow
FileTreeStateAdapter

DO NOT modify :treeview.

PHASE 7

Implement:

SideActionBar
SideActionItem
SelectionToolbar

PHASE 8

Implement dialogs.

PHASE 9

Connect everything to:

DualPaneViewModel

DualPaneReducer

DualPaneEvent

PHASE 10

Visual audit against screenshots.

PHASE 11

Architecture audit.

============================================================
ACCEPTANCE CRITERIA
============================================================

BUILD:

./gradlew assembleDebug

must pass.

No unresolved references.

No circular module dependency.

No filesystem API inside :treeview.

No FileItem import inside :treeview.

No repository import inside reusable UI component.

No usecase call from composable.

No duplicated selection state.

No duplicated currentLocation state.

No duplicated operation state.

No God Class.

============================================================
FINAL AUDIT OUTPUT
============================================================

After implementation, DO NOT simply say:

"Done."

Produce an audit report containing:

1. Files created.
2. Files modified.
3. Files untouched.
4. Responsibility of every UI class.
5. Dependency graph.
6. State ownership graph.
7. Event flow.
8. TreeView integration.
9. Backend/UI boundary.
10. God Class audit.
11. SRP audit.
12. Duplicate state audit.
13. Filesystem leakage audit.
14. TreeView isolation audit.
15. Compose UI audit.
16. Build result.
17. Remaining technical debt.

For every class state:

CLASS:
RESPONSIBILITY:
INPUT:
OUTPUT:
DEPENDENCIES:
FORBIDDEN RESPONSIBILITIES:

============================================================
HARD STOP RULE
============================================================

Jika menemukan bahwa implementasi membutuhkan perubahan pada
:treeview karena kebutuhan File Manager:

STOP.

Jangan memasukkan domain File Manager ke :treeview.

Buat adapter di :filemanager-ui.

Jika menemukan satu class mulai menggabungkan beberapa responsibility:

STOP.

Pisahkan berdasarkan SRP sebelum melanjutkan.

Jika tidak yakin apakah suatu logic termasuk UI atau backend:

STOP.

Audit dependency direction terlebih dahulu.

============================================================
FINAL PRINCIPLE
============================================================

UI = render state + emit events.

ViewModel = screen-level state holder + orchestration.

Reducer = pure state transformation.

OperationHandler = operation orchestration.

UseCase = business/domain operation.

Repository/storage = data access.

TreeView = generic tree UI/state.

FileTree adapter = bridge antara File Manager dan generic TreeView.

Mapper = data transformation.

Dialog = input/confirmation/presentation.

Tidak boleh ada satu class yang mengambil semua tanggung jawab tersebut.

END ARCHITECTURE GUARD
============================================================
