// ════════════════════════════════════════════════════════════
//  app.js — JavaScript general de Playres
//  Secciones:
//    1. NAVBAR scroll
//    2. CATÁLOGO — filtros, galería, paginación
//    3. PRODUCT DETAIL — galería, días, tabs
//    4. ADMIN — tabla, modales, CRUD
// ════════════════════════════════════════════════════════════

// ── 1. NAVBAR SCROLL ─────────────────────────────────────────
const navbar = document.querySelector(".navbar-playres");
if (navbar) {
  window.addEventListener("scroll", () => {
    navbar.classList.toggle("scrolled", window.scrollY > 30);
  });
}

// ════════════════════════════════════════════════════════════
//  2. CATÁLOGO (solo se ejecuta si existe #gamesGrid)
// ════════════════════════════════════════════════════════════
if (document.getElementById("gamesGrid")) {
  let filteredGames = [];
  let currentPage = 1;
  const perPage = 8;
  let activeGenre = "";
  let activePlatform = "";

  // Leer parámetros de URL para aplicar filtros
  const urlParams = new URLSearchParams(window.location.search);
  const genreParam = urlParams.get("genre");

  cargarJuegosAPI().then(() => {
    filteredGames = [...juegos];

    // Si hay un género en la URL, aplicarlo
    if (genreParam) {
      activeGenre = genreParam;
      // Actualizar UI para mostrar el filtro activo
      document.querySelectorAll(".genre-filter-tag").forEach((tag) => {
        if (tag.textContent.includes(genreParam)) {
          tag.classList.add("active");
        } else {
          tag.classList.remove("active");
        }
      });
    }

    filterGames();
  });

  // Filtrar juegos
  window.setFilter = function (type, value, el) {
    if (type === "genre") {
      activeGenre = value;
      document
        .querySelectorAll(".genre-filter-tag")
        .forEach((t) => t.classList.remove("active"));
    } else {
      activePlatform = value;
      document
        .querySelectorAll(".platform-filter-tag")
        .forEach((t) => t.classList.remove("active"));
    }
    if (el) el.classList.add("active");
    filterGames();
  };

  window.filterGames = function () {
    const q = document.getElementById("searchInput")
      ? document.getElementById("searchInput").value.toLowerCase()
      : "";
    filteredGames = juegos.filter(
      (g) =>
        (!q ||
          g.title.toLowerCase().includes(q) ||
          g.genre.toLowerCase().includes(q) ||
          g.platform.toLowerCase().includes(q)) &&
        (!activeGenre || g.genre === activeGenre) &&
        (!activePlatform || g.platform === activePlatform),
    );
    currentPage = 1;
    renderGames();
  };

  function renderGames() {
    const start = (currentPage - 1) * perPage;
    const slice = filteredGames.slice(start, start + perPage);
    const totalPages = Math.ceil(filteredGames.length / perPage);
    const grid = document.getElementById("gamesGrid");

    document.getElementById("resultCount").textContent =
      `Mostrando ${filteredGames.length} juego${filteredGames.length !== 1 ? "s" : ""}`;
    document.getElementById("pageIndicator").textContent =
      `Página ${currentPage} de ${totalPages || 1}`;

    grid.innerHTML =
      slice.length === 0
        ? `<div class="col-12 catalog-empty"><p>No se encontraron juegos con ese filtro.</p></div>`
        : slice
            .map(
              (g) => `
            <div class="col-lg-3 col-md-4 col-sm-6">
              <div class="game-card">
                <a href="product-detail.html" class="catalog-card-link">
                  <div class="game-thumbnail ${g.bg}">
                    <div class="game-tags">
                      <span class="game-tag">${g.genre}</span>
                      ${g.stock === 0 ? `<span class="game-tag-top">AGOTADO</span>` : ""}
                    </div>
                    ${
                      g.imagenUrl
                        ? `<img class="game-cover" src="${g.imagenUrl}" alt="${g.title}" />`
                        : `<span style="font-size:3rem">${g.emoji}</span>`
                    }
                  </div>
                </a>
                <div class="game-card-info">
                  <div class="game-platform">${g.platform}</div>
                  <h3 class="game-title-catalog">${g.title}</h3>
                  <div class="game-bottom">
                    <div class="game-price">$${g.price.toLocaleString("es-CO")}<span>/día</span></div>
                    <div><span class="game-stars">★★★★★</span><span class="game-rating">${g.rating}</span></div>
                  </div>
                  <a href="product-detail.html"
                     class="catalog-btn-ver ${g.stock === 0 ? "disabled" : ""}"
                     ${g.stock === 0 ? 'style="pointer-events:none;opacity:0.5"' : ""}>
                    ${g.stock === 0 ? "Agotado" : "Ver detalles →"}
                  </a>
                </div>
              </div>
            </div>
          `,
            )
            .join("");

    // Paginación Bootstrap
    const pag = document.getElementById("pagination");
    pag.innerHTML = `
        <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
          <a class="page-link" href="#catalogo" onclick="goPage(${currentPage - 1})">←</a>
        </li>`;

    for (let i = 1; i <= totalPages; i++) {
      pag.innerHTML += `
          <li class="page-item ${i === currentPage ? "active" : ""}">
            <a class="page-link" href="#catalogo" onclick="goPage(${i})">${i}</a>
          </li>`;
    }

    pag.innerHTML += `
        <li class="page-item ${currentPage >= totalPages ? "disabled" : ""}">
          <a class="page-link" href="#catalogo" onclick="goPage(${currentPage + 1})">→</a>
        </li>`;
  }

  window.goPage = function (n) {
    const totalPages = Math.ceil(filteredGames.length / perPage);
    if (n < 1 || n > totalPages) return;
    currentPage = n;
    renderGames();
  };

  // Iniciar catálogo
  renderGames();
}

// ════════════════════════════════════════════════════════════
//  3. PRODUCT DETAIL (solo si existe #galleryMain)
// ════════════════════════════════════════════════════════════
if (document.getElementById("galleryMain")) {
  // Cambiar imagen galería
  window.changeGallery = function (el, emoji, c1, c2) {
    document
      .querySelectorAll(".gallery-thumb")
      .forEach((t) => t.classList.remove("active"));
    el.classList.add("active");
    document.getElementById("galleryEmoji").textContent = emoji;
    document.getElementById("galleryMain").style.background =
      `linear-gradient(135deg, ${c1}, ${c2})`;
  };

  // Selector de días y cálculo de total
  const PRICE = 9900;
  window.selectDays = function (el, days) {
    document
      .querySelectorAll(".day-btn")
      .forEach((b) => b.classList.remove("active"));
    el.classList.add("active");
    document.getElementById("totalDisplay").textContent =
      "$" + (PRICE * days).toLocaleString("es-CO");
  };

  // Cambiar tabs
  window.switchTab = function (el, tabId) {
    document
      .querySelectorAll(".detail-tab")
      .forEach((t) => t.classList.remove("active"));
    document
      .querySelectorAll(".tab-content-playres")
      .forEach((t) => t.classList.remove("active"));
    el.classList.add("active");
    document.getElementById(tabId).classList.add("active");
  };
}

// ════════════════════════════════════════════════════════════
//  4. ADMIN PANEL (solo si existe #tableBody)
// ════════════════════════════════════════════════════════════
if (document.getElementById("tableBody")) {
  let products = []; // se llena desde el backend
  let filteredProds = [];
  let currentPage = 1;
  const perPage = 8;
  let deleteIndex = -1;

  window.cargarProductos = async function () {
    try {
      products = await ProductoAPI.listar();
      filteredProds = [...products];
      renderTable();
    } catch (e) {
      showToast("danger", "❌", "No se pudo conectar al backend: " + e.message);
    }
  };

  function renderTable() {
    const start = (currentPage - 1) * perPage;
    const slice = filteredProds.slice(start, start + perPage);
    const totalPages = Math.ceil(filteredProds.length / perPage);

    document.getElementById("tableBody").innerHTML = slice
      .map((p) => {
        const realIdx = products.indexOf(p);
        return `
        <tr>
          <td>
            <div class="tbl-game-info">
              <div class="tbl-thumb ${p.bg}">${p.emoji}</div>
              <div>
                <div class="tbl-game-name">${p.title}</div>
                <div class="tbl-game-id">${p.id}</div>
              </div>
            </div>
          </td>
          <td><span class="platform-tbl">${p.platform}</span></td>
          <td>${p.genre}</td>
          <td class="tbl-price">$${p.price.toLocaleString("es-CO")}</td>
          <td><span class="tbl-stars">${"★".repeat(Math.floor(p.rating))}</span> <span class="tbl-rating-num">${p.rating}</span></td>
          <td><span class="status-badge ${p.status}">${p.status.charAt(0).toUpperCase() + p.status.slice(1)}</span></td>
          <td class="${p.stock === 0 ? "tbl-stock-out" : "tbl-stock"}">${p.stock}</td>
          <td>
            <div class="action-btns">
              <button class="btn-edit" onclick="openEditModal(${realIdx})">✏️ Editar</button>
              <button class="btn-delete" onclick="openDeleteModal(${realIdx})">🗑️</button>
            </div>
          </td>
        </tr>`;
      })
      .join("");

    // Paginación
    document.getElementById("prevPage").disabled = currentPage === 1;
    document.getElementById("nextPage").disabled = currentPage >= totalPages;
    document
      .getElementById("page1")
      .classList.toggle("active", currentPage === 1);
    document
      .getElementById("page2")
      .classList.toggle("active", currentPage === 2);
    document.getElementById("page2").style.display =
      totalPages < 2 ? "none" : "flex";

    const from = start + 1;
    const to = Math.min(start + perPage, filteredProds.length);
    document.getElementById("pagInfo").textContent =
      `Mostrando ${from}–${to} de ${filteredProds.length} productos`;
    document.getElementById("resultCount").textContent =
      `Mostrando ${filteredProds.length} producto${filteredProds.length !== 1 ? "s" : ""}`;

    // Stats
    document.getElementById("totalCount").textContent = products.length;
    document.getElementById("availCount").textContent = products.filter(
      (p) => p.status === "disponible",
    ).length;
    document.getElementById("reservedCount").textContent = products.filter(
      (p) => p.status === "reservado",
    ).length;
    document.getElementById("outCount").textContent = products.filter(
      (p) => p.status === "agotado",
    ).length;
  }

  window.filterTable = function () {
    const q = document.getElementById("searchInput").value.toLowerCase();
    const plt = document.getElementById("platformFilter").value;
    const sts = document.getElementById("statusFilter").value;
    filteredProds = products.filter(
      (p) =>
        (!q ||
          p.title.toLowerCase().includes(q) ||
          p.id.toLowerCase().includes(q)) &&
        (!plt || p.platform === plt) &&
        (!sts || p.status === sts),
    );
    currentPage = 1;
    renderTable();
  };

  window.changePage = function (dir) {
    currentPage += dir;
    renderTable();
  };
  window.goPage = function (n) {
    currentPage = n;
    renderTable();
  };

  window.openAddModal = function () {
    document.getElementById("modalTitle").textContent = "➕ Agregar producto";
    document.getElementById("editIndex").value = -1;
    ["fTitle", "fPrice", "fStock", "fRating"].forEach(
      (id) => (document.getElementById(id).value = ""),
    );
    document.getElementById("fPlatform").value = "PS5";
    document.getElementById("fGenre").value = "RPG";
    document.getElementById("fStatus").value = "disponible";
    document.getElementById("fEmoji").value = "🌌";
    new bootstrap.Modal(document.getElementById("productModal")).show();
  };

  window.openEditModal = function (idx) {
    const p = products[idx];
    document.getElementById("modalTitle").textContent = "✏️ Editar producto";
    document.getElementById("editIndex").value = idx;
    document.getElementById("fTitle").value = p.title;
    document.getElementById("fPrice").value = p.price;
    document.getElementById("fStock").value = p.stock;
    document.getElementById("fRating").value = p.rating;
    document.getElementById("fPlatform").value = p.platform;
    document.getElementById("fGenre").value = p.genre;
    document.getElementById("fStatus").value = p.status;
    document.getElementById("fEmoji").value = p.emoji;
    new bootstrap.Modal(document.getElementById("productModal")).show();
  };

  window.saveProduct = function () {
    const idx = parseInt(document.getElementById("editIndex").value);
    const title = document.getElementById("fTitle").value.trim();
    const price = parseInt(document.getElementById("fPrice").value);
    if (!title || !price) {
      showToast("danger", "⚠️", "Completa título y precio.");
      return;
    }

    const bgMap = {
      PS5: "game-thumb-purple",
      Xbox: "game-thumb-red",
      Switch: "game-thumb-green",
      PC: "game-thumb-blue",
    };
    const platform = document.getElementById("fPlatform").value;

    const prod = {
      id:
        idx >= 0
          ? products[idx].id
          : "PLY-" + String(products.length + 1).padStart(3, "0"),
      title,
      platform,
      genre: document.getElementById("fGenre").value,
      price,
      rating: parseFloat(document.getElementById("fRating").value) || 4.5,
      stock:
        document.getElementById("fStock").value !== ""
          ? parseInt(document.getElementById("fStock").value)
          : idx >= 0
            ? products[idx].stock
            : 0,
      status: document.getElementById("fStatus").value,
      emoji: document.getElementById("fEmoji").value,
      bg: bgMap[platform] || "game-thumb-purple",
      tag: "",
      tagClass: "",
      description: "",
    };

    if (idx >= 0) {
      products[idx] = prod;
      showToast("success", "✅", "Producto actualizado.");
    } else {
      products.push(prod);
      showToast("success", "✅", "Producto agregado.");
    }

    filteredProds = [...products];
    filterTable();
    bootstrap.Modal.getInstance(document.getElementById("productModal")).hide();
  };

  window.openDeleteModal = function (idx) {
    deleteIndex = idx;
    document.getElementById("deleteGameName").textContent =
      '"' + products[idx].title + '"';
    new bootstrap.Modal(document.getElementById("deleteModal")).show();
  };

  window.confirmDelete = function () {
    if (deleteIndex < 0) return;
    const name = products[deleteIndex].title;
    products.splice(deleteIndex, 1);
    deleteIndex = -1;
    filteredProds = [...products];
    filterTable();
    bootstrap.Modal.getInstance(document.getElementById("deleteModal")).hide();
    showToast("danger", "🗑️", `"${name}" eliminado.`);
  };

  let toastTimer;
  function showToast(type, icon, msg) {
    const el = document.getElementById("toastEl");
    document.getElementById("toastIcon").textContent = icon;
    document.getElementById("toastMsg").textContent = msg;
    el.className = `playres-toast ${type} show`;
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => el.classList.remove("show"), 3000);
  }

  // ── IMPORTAR DESDE RAWG ──────────────────────────────────
  window.openImportModal = function () {
    document.getElementById("rawgSearchInput").value = "";
    document.getElementById("rawgSearchResults").style.display = "none";
    document.getElementById("rawgImportForm").style.display = "none";
    document.getElementById("importConfirmBtn").style.display = "none";
    document.getElementById("rawgResultsList").innerHTML = "";
    document.getElementById("selectedRawgId").value = "";
    new bootstrap.Modal(document.getElementById("importRawgModal")).show();
  };

  window.searchRawgGames = async function () {
    const query = document.getElementById("rawgSearchInput").value.trim();
    if (!query) {
      showToast("warning", "⚠️", "Ingresa el nombre de un juego.");
      return;
    }

    try {
      showToast("info", "🔍", "Buscando en RAWG...");
      const results = await fetch(
        `http://localhost:8080/api/rawg/buscar?q=${encodeURIComponent(query)}`,
      ).then((r) => r.json());

      if (!results || results.length === 0) {
        showToast("warning", "⚠️", "No se encontraron juegos con ese nombre.");
        document.getElementById("rawgSearchResults").style.display = "none";
        return;
      }

      // Mostrar resultados
      const resultsList = document.getElementById("rawgResultsList");
      resultsList.innerHTML = results
        .map((game) => {
          const gameName = game.nombre || game.name || "Juego sin nombre";
          const gameId = game.id;
          return `
        <div style="padding: 12px; border-bottom: 1px solid var(--border); cursor: pointer; transition: all 0.2s; background: rgba(255,255,255,0.02);" 
             onclick="selectRawgGame(${gameId}, '${gameName.replace(/'/g, "\\'")}')">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
              <strong style="color: #fff;">${gameName}</strong>
              <div style="font-size: 0.85rem; color: var(--muted); margin-top: 4px;">
                Géneros: ${game.genres && game.genres.length > 0 ? game.genres.map((g) => g.name || g).join(", ") : "N/A"}
              </div>
            </div>
            <div style="color: var(--red); font-weight: bold;">Seleccionar →</div>
          </div>
        </div>
      `;
        })
        .join("");

      document.getElementById("rawgSearchResults").style.display = "block";
      showToast("success", "✅", `Se encontraron ${results.length} juego(s).`);
    } catch (error) {
      showToast("danger", "❌", "Error al buscar: " + error.message);
    }
  };

  window.selectRawgGame = function (rawgId, gameName) {
    document.getElementById("selectedRawgId").value = rawgId;
    document.getElementById("selectedGameTitle").value = gameName;
    document.getElementById("rawgImportForm").style.display = "block";
    document.getElementById("importConfirmBtn").style.display = "inline-block";
    document.getElementById("rawgSearchResults").style.display = "none";
  };

  window.confirmImportGame = async function () {
    const rawgId = parseInt(document.getElementById("selectedRawgId").value);
    const plataforma = document.getElementById("importPlatform").value;
    const precio = parseInt(document.getElementById("importPrice").value);
    const stock = parseInt(document.getElementById("importStock").value);

    if (!rawgId || !precio || !stock) {
      showToast("warning", "⚠️", "Completa todos los campos.");
      return;
    }

    try {
      showToast("info", "⏳", "Importando juego...");
      const result = await ProductoAPI.importar({
        rawgId,
        precio,
        stock,
        plataforma,
      });

      // Mapear campos del backend al formato del frontend
      const bgMap = {
        PS5: "game-thumb-purple",
        Xbox: "game-thumb-red",
        Switch: "game-thumb-green",
        PC: "game-thumb-blue",
      };

      const productoMapeado = {
        id: result.id,
        title: result.titulo,
        platform: result.plataforma,
        genre: result.genero || "Acción",
        price: result.precio,
        rating: result.rating || 4.5,
        stock: result.stock,
        status: result.estado ? result.estado.toLowerCase() : "disponible",
        emoji: result.emoji || "🎮",
        bg: bgMap[result.plataforma] || "game-thumb-purple",
        tag: "",
        tagClass: "",
        description: "",
      };

      // Agregar a la lista local
      products.push(productoMapeado);
      filteredProds = [...products];
      filterTable();

      bootstrap.Modal.getInstance(
        document.getElementById("importRawgModal"),
      ).hide();
      showToast("success", "✅", `"${result.titulo}" importado correctamente.`);
    } catch (error) {
      showToast("danger", "❌", "Error al importar: " + error.message);
    }
  };

  // Iniciar admin
  cargarProductos();
}

// ==========================================
// 1. REGISTRO DE USUARIO — llama al backend
// ==========================================
const registerForm = document.getElementById("registerForm");
if (registerForm) {
  registerForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const nombre = document.getElementById("regNombre").value.trim();
    const apellido = document.getElementById("regApellido").value.trim();
    const email = document.getElementById("regEmail").value.trim();
    const password = document.getElementById("regPassword").value.trim();

    const errorMsg = document.getElementById("registerError");
    const successMsg = document.getElementById("successMessage");

    try {
      await AuthAPI.registro({
        nombre: nombre + " " + apellido,
        email,
        password,
      });

      // Registro exitoso: mostrar mensaje y cerrar modal
      if (successMsg) successMsg.classList.remove("d-none");
      registerForm.reset();

      setTimeout(() => {
        const modal = bootstrap.Modal.getInstance(
          document.getElementById("registerModal"),
        );
        modal.hide();
        if (successMsg) successMsg.classList.add("d-none");
      }, 2000);
    } catch (err) {
      // El backend lanza "Email ya existe" cuando el correo está duplicado
      if (errorMsg) {
        errorMsg.textContent = err.message.includes("ya existe")
          ? "Este correo ya está registrado."
          : "Error al registrar. Intenta de nuevo.";
        errorMsg.classList.remove("d-none");
      }
    }
  });
}

// ==========================================
// 2. LOGIN DE USUARIO — llama al backend
// ==========================================
const loginForm = document.getElementById("loginForm");
if (loginForm) {
  loginForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value.trim();
    const errorMsg = document.getElementById("loginError");

    try {
      const usuario = await AuthAPI.login({ email, password });

      // Guardamos solo datos no sensibles en sessionStorage
      sessionStorage.setItem(
        "usuarioActivo",
        JSON.stringify({
          id: usuario.id,
          nombre: usuario.nombre,
          email: usuario.email,
          rol: usuario.rol,
        }),
      );

      const modal = bootstrap.Modal.getInstance(
        document.getElementById("loginModal"),
      );
      modal.hide();
      loginForm.reset();
      if (errorMsg) errorMsg.classList.add("d-none");

      actualizarInterfaz();

      // Si es admin, redirigir al panel
      if (usuario.rol === "ADMIN") {
        window.location.href = "admin.html";
      }
    } catch (err) {
      if (errorMsg) {
        errorMsg.textContent = "Correo o contraseña incorrectos.";
        errorMsg.classList.remove("d-none");
      }
    }
  });
}

// ==========================================
// 3. CAMBIO DE LOS BOTONES POR EL PERFIL
// ==========================================
function actualizarInterfaz() {
  const sesion = JSON.parse(sessionStorage.getItem("usuarioActivo"));

  const botonesAuth = document.getElementById("navAuthButtons");
  const perfilUsuario = document.getElementById("navUserProfile");
  const nombreTxt = document.getElementById("userNameDisplay");
  const avatarCirculo = document.getElementById("userAvatarInitials");

  if (sesion) {
    if (botonesAuth) botonesAuth.classList.add("d-none");
    if (perfilUsuario) perfilUsuario.classList.remove("d-none");

    if (nombreTxt) nombreTxt.textContent = sesion.nombre;

    if (avatarCirculo) {
      const partes = sesion.nombre.split(" ");
      const iniciales =
        (partes[0]?.charAt(0) ?? "") + (partes[1]?.charAt(0) ?? "");
      avatarCirculo.textContent = iniciales.toUpperCase();
    }
  } else {
    if (botonesAuth) botonesAuth.classList.remove("d-none");
    if (perfilUsuario) perfilUsuario.classList.add("d-none");
  }
}

// ==========================================
// 4. CERRAR SESIÓN
// ==========================================
window.cerrarSesion = async function () {
  try {
    await AuthAPI.logout();
  } catch (_) {
    // El backend solo responde con un mensaje, no importa si falla
  }
  sessionStorage.removeItem("usuarioActivo");
  actualizarInterfaz();
};

// Al cargar la página, verificar si ya hay sesión
document.addEventListener("DOMContentLoaded", actualizarInterfaz);

/* ACTIVAR SELECCIÓN DE CATEGORÍAS */

document.querySelectorAll(".filter-tag").forEach((tag) => {
  tag.addEventListener("click", function () {
    document
      .querySelectorAll(".filter-tag")
      .forEach((t) => t.classList.remove("active"));

    this.classList.add("active");
  });
});

/* =========================================
   AGREGAR NUEVA CATEGORÍA DESDE MODAL
========================================= */

document.addEventListener("DOMContentLoaded", () => {
  const categoryForm = document.getElementById("addCategoryForm");

  if (!categoryForm) return;

  categoryForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const nombreCategoria = document
      .getElementById("categoryNameInput")
      .value.trim();

    if (!nombreCategoria) return;

    const catGrid = document.querySelector(".cat-grid");

    const nuevaCategoria = document.createElement("a");
    nuevaCategoria.href = "#";
    nuevaCategoria.classList.add("cat-card");

    nuevaCategoria.innerHTML = `
      <div style="font-size:40px; margin-bottom:10px;">🎮</div>
      <h3 style="color:white; font-size:18px; margin:5px 0;">
        ${nombreCategoria}
      </h3>
      <p style="color:#8b9bb4; font-size:14px;">
        Nuevo
    `;

    catGrid.appendChild(nuevaCategoria);

    categoryForm.reset();

    const modal = bootstrap.Modal.getInstance(
      document.getElementById("addCategoryModal"),
    );

    modal.hide();
  });
});

/* DISPONIBILIDAD DE JUEGOS */

document.querySelectorAll(".game-card").forEach((card) => {
  const disponible = Math.random() > 0.3;

  const badge = document.createElement("div");
  badge.className = disponible
    ? "status-badge disponible"
    : "status-badge agotado";

  badge.style.position = "absolute";
  badge.style.bottom = "10px";
  badge.style.left = "10px";

  badge.textContent = disponible ? "Disponible" : "Agotado";

  const thumb = card.querySelector(".game-thumbnail");
  thumb.style.position = "relative";
  thumb.appendChild(badge);
});

/* =========================================
   MARCAR / ELIMINAR JUEGOS FAVORITOS
========================================= */

let favoritos = [];

document.querySelectorAll(".game-card").forEach((card) => {
  const favBtn = document.createElement("button");
  favBtn.innerHTML = "⭐";
  favBtn.className = "fav-btn";

  favBtn.style.position = "absolute";
  favBtn.style.top = "260px";
  favBtn.style.right = "10px";

  const thumb = card.querySelector(".game-thumbnail");
  thumb.style.position = "relative";
  thumb.appendChild(favBtn);

  favBtn.addEventListener("click", (e) => {
    e.stopPropagation();

    const titulo = card.querySelector(".game-title-catalog").textContent;

    // SI YA ES FAVORITO → ELIMINAR
    if (card.classList.contains("favorito")) {
      card.classList.remove("favorito");

      favoritos = favoritos.filter(
        (game) =>
          game.querySelector(".game-title-catalog").textContent !== titulo,
      );
    }
    // SI NO ES FAVORITO → AGREGAR
    else {
      card.classList.add("favorito");

      favoritos.push(card.cloneNode(true));
    }

    renderFavoritos();
  });
});

/* MOSTRAR LISTA DE FAVORITOS */

function renderFavoritos() {
  const grid = document.getElementById("favoritesGrid");
  if (!grid) return;

  grid.innerHTML = "";

  if (favoritos.length === 0) {
    grid.innerHTML = "<p style='color:gray;'>Aún no tienes favoritos.</p>";
    return;
  }

  favoritos.forEach((game) => {
    grid.appendChild(game);
  });
}
