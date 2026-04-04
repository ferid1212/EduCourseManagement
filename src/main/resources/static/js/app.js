// === EduCourse Management System — Main Application ===

const App = {
  currentSection: 'dashboard',
  role: null,
  email: null,

  init() {
    if (API.getToken() && !API.isTokenExpired()) {
      this.role = API.getUserRole();
      this.email = API.getUserEmail();
      this.showApp();
    } else {
      API.removeToken();
      this.showAuth();
    }
    this.bindGlobalEvents();
  },

  bindGlobalEvents() {
    // Auth tabs
    document.querySelectorAll('.auth-tab').forEach(tab => {
      tab.addEventListener('click', () => {
        document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));
        tab.classList.add('active');
        document.getElementById('login-form').classList.toggle('hidden', tab.dataset.tab !== 'login');
        document.getElementById('register-form').classList.toggle('hidden', tab.dataset.tab !== 'register');
      });
    });

    // Login
    document.getElementById('login-form').addEventListener('submit', async (e) => {
      e.preventDefault();
      const btn = e.target.querySelector('button[type="submit"]');
      btn.disabled = true;
      btn.innerHTML = '<span class="spinner"></span> Giriş edilir...';
      try {
        const res = await API.auth.login(
          document.getElementById('login-email').value,
          document.getElementById('login-password').value
        );
        API.setToken(res.token);
        this.role = API.getUserRole();
        this.email = API.getUserEmail();
        this.showApp();
        Toast.success('Uğurla daxil oldunuz!');
      } catch (err) {
        Toast.error(err.message);
      } finally {
        btn.disabled = false;
        btn.innerHTML = '🔐 Daxil ol';
      }
    });

    // Register
    document.getElementById('register-form').addEventListener('submit', async (e) => {
      e.preventDefault();
      const btn = e.target.querySelector('button[type="submit"]');
      btn.disabled = true;
      btn.innerHTML = '<span class="spinner"></span> Qeydiyyat...';
      try {
        const res = await API.auth.register({
          firstName: document.getElementById('reg-firstname').value,
          lastName: document.getElementById('reg-lastname').value,
          email: document.getElementById('reg-email').value,
          age: parseInt(document.getElementById('reg-age').value) || null,
          password: document.getElementById('reg-password').value,
          phone: document.getElementById('reg-phone').value || null,
        });
        API.setToken(res.token);
        this.role = API.getUserRole();
        this.email = API.getUserEmail();
        this.showApp();
        Toast.success('Qeydiyyat uğurla tamamlandı!');
      } catch (err) {
        Toast.error(err.message);
      } finally {
        btn.disabled = false;
        btn.innerHTML = '🚀 Qeydiyyatdan keç';
      }
    });

    // Mobile toggle
    document.getElementById('mobile-toggle')?.addEventListener('click', () => {
      document.querySelector('.sidebar').classList.toggle('open');
    });

    // Logout
    document.getElementById('btn-logout')?.addEventListener('click', () => {
      API.removeToken();
      this.role = null;
      this.email = null;
      this.showAuth();
      Toast.info('Çıxış edildi.');
    });

    // Modal close on overlay click
    document.addEventListener('click', (e) => {
      if (e.target.classList.contains('modal-overlay')) {
        e.target.classList.remove('active');
      }
      if (e.target.classList.contains('modal-close')) {
        e.target.closest('.modal-overlay').classList.remove('active');
      }
    });
  },

  showAuth() {
    document.getElementById('auth-screen').style.display = 'flex';
    document.getElementById('app-screen').classList.remove('active');
  },

  showApp() {
    document.getElementById('auth-screen').style.display = 'none';
    document.getElementById('app-screen').classList.add('active');
    this.setupSidebar();
    this.navigate('dashboard');
  },

  setupSidebar() {
    const nav = document.getElementById('sidebar-nav');
    const role = this.role;

    // User info
    const initials = this.email ? this.email.charAt(0).toUpperCase() : '?';
    document.getElementById('user-avatar').textContent = initials;
    document.getElementById('user-email').textContent = this.email || '';
    document.getElementById('user-role').textContent = role || '';

    let html = '';
    html += '<div class="nav-section-title">Əsas</div>';
    html += this.navItem('dashboard', '📊', 'İdarə Paneli');

    if (role === 'ADMIN') {
      html += '<div class="nav-section-title">İdarəetmə</div>';
      html += this.navItem('courses', '📚', 'Kurslar');
      html += this.navItem('teachers', '👨‍🏫', 'Müəllimlər');
      html += this.navItem('students', '🎓', 'Tələbələr');
      html += this.navItem('lessons', '📖', 'Dərslər');
      html += this.navItem('enrollments', '📋', 'Qeydiyyatlar');
    } else if (role === 'TEACHER') {
      html += '<div class="nav-section-title">Müəllim</div>';
      html += this.navItem('courses', '📚', 'Kurslar');
      html += this.navItem('lessons', '📖', 'Dərslər');
    } else if (role === 'STUDENT') {
      html += '<div class="nav-section-title">Tələbə</div>';
      html += this.navItem('courses', '📚', 'Kurslar');
      html += this.navItem('enrollments', '📋', 'Qeydiyyatlarım');
    }

    nav.innerHTML = html;

    // Nav click
    nav.querySelectorAll('.nav-item').forEach(item => {
      item.addEventListener('click', () => {
        this.navigate(item.dataset.section);
        document.querySelector('.sidebar').classList.remove('open');
      });
    });
  },

  navItem(section, icon, label) {
    return `<div class="nav-item" data-section="${section}">
      <span class="nav-icon">${icon}</span>
      <span>${label}</span>
    </div>`;
  },

  navigate(section) {
    this.currentSection = section;

    // Update nav active
    document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));
    document.querySelector(`.nav-item[data-section="${section}"]`)?.classList.add('active');

    // Show section
    document.querySelectorAll('.section-page').forEach(s => s.classList.remove('active'));
    document.getElementById(`section-${section}`)?.classList.add('active');

    // Load section data
    this.loadSection(section);
  },

  async loadSection(section) {
    switch (section) {
      case 'dashboard': await Dashboard.load(); break;
      case 'courses': await Courses.load(); break;
      case 'teachers': await Teachers.load(); break;
      case 'students': await Students.load(); break;
      case 'lessons': await Lessons.load(); break;
      case 'enrollments': await Enrollments.load(); break;
    }
  },
};

// === Toast Notifications ===
const Toast = {
  container: null,

  init() {
    this.container = document.getElementById('toast-container');
  },

  show(message, type = 'info') {
    if (!this.container) this.init();
    const icons = { success: '✅', error: '❌', info: 'ℹ️' };
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `<span>${icons[type] || ''}</span><span>${message}</span>`;
    this.container.appendChild(toast);
    setTimeout(() => {
      toast.style.opacity = '0';
      toast.style.transform = 'translateX(60px)';
      setTimeout(() => toast.remove(), 300);
    }, 3500);
  },

  success(msg) { this.show(msg, 'success'); },
  error(msg) { this.show(msg, 'error'); },
  info(msg) { this.show(msg, 'info'); },
};

// === Helpers ===
function formatDate(dateStr) {
  if (!dateStr) return '—';
  const d = new Date(dateStr);
  return d.toLocaleDateString('az-AZ', { year: 'numeric', month: 'short', day: 'numeric' });
}

function statusBadge(isActive) {
  return isActive === true
    ? '<span class="badge badge-active">Aktiv</span>'
    : '<span class="badge badge-inactive">Deaktiv</span>';
}

function enrollmentBadge(status) {
  const map = {
    ACTIVE: 'badge-active',
    PENDING: 'badge-pending',
    COMPLETED: 'badge-completed',
    CANCELLED: 'badge-cancelled',
    SUSPENDED: 'badge-suspended',
  };
  return `<span class="badge ${map[status] || 'badge-pending'}">${status}</span>`;
}

function openModal(id) {
  document.getElementById(id)?.classList.add('active');
}

function closeModal(id) {
  document.getElementById(id)?.classList.remove('active');
}

function showLoading(containerId) {
  const el = document.getElementById(containerId);
  if (!el) return;
  const colCount = el.closest('table')?.querySelectorAll('thead th').length || 1;
  el.innerHTML = `<tr><td colspan="${colCount}" style="text-align:center; padding: 40px;">
    <div class="loading-overlay" style="position:static; background:none; height:auto;">
      <span class="spinner"></span> Yüklənir...
    </div>
  </td></tr>`;
}

function showEmpty(containerId, icon, title, desc) {
  const el = document.getElementById(containerId);
  if (!el) return;
  const colCount = el.closest('table')?.querySelectorAll('thead th').length || 1;
  el.innerHTML = `<tr><td colspan="${colCount}" style="text-align:center; padding: 40px;">
    <div class="empty-state" style="margin:0;">
      <div class="empty-icon">${icon}</div>
      <h3>${title}</h3>
      <p>${desc}</p>
    </div>
  </td></tr>`;
}

// === Dashboard ===
const Dashboard = {
  async load() {
    const role = App.role;
    const container = document.getElementById('dashboard-stats');
    container.innerHTML = '';

    try {
      if (role === 'ADMIN') {
        const [courses, teachers, students, enrollments] = await Promise.allSettled([
          API.courses.getAll(),
          API.teachers.getAll(),
          API.students.getAll(),
          API.enrollments.getAll(),
        ]);

        const cCount = courses.status === 'fulfilled' ? (Array.isArray(courses.value) ? courses.value.length : 0) : 0;
        const tCount = teachers.status === 'fulfilled' ? (Array.isArray(teachers.value) ? teachers.value.length : 0) : 0;
        const sCount = students.status === 'fulfilled' ? (Array.isArray(students.value) ? students.value.length : 0) : 0;
        const eCount = enrollments.status === 'fulfilled' ? (Array.isArray(enrollments.value) ? enrollments.value.length : 0) : 0;

        container.innerHTML = `
          ${this.statCard('📚', cCount, 'Ümumi Kurslar')}
          ${this.statCard('👨‍🏫', tCount, 'Müəllimlər')}
          ${this.statCard('🎓', sCount, 'Tələbələr')}
          ${this.statCard('📋', eCount, 'Qeydiyyatlar')}
        `;
      } else if (role === 'TEACHER') {
        const [courses, lessons] = await Promise.allSettled([
          API.courses.getActive(),
          API.lessons.getActive(),
        ]);
        const cCount = courses.status === 'fulfilled' ? (Array.isArray(courses.value) ? courses.value.length : 0) : 0;
        const lCount = lessons.status === 'fulfilled' ? (Array.isArray(lessons.value) ? lessons.value.length : 0) : 0;

        container.innerHTML = `
          ${this.statCard('📚', cCount, 'Aktiv Kurslar')}
          ${this.statCard('📖', lCount, 'Aktiv Dərslər')}
        `;
      } else if (role === 'STUDENT') {
        const [courses, enrollments] = await Promise.allSettled([
          API.courses.getActive(),
          API.enrollments.getAll(),
        ]);
        const cCount = courses.status === 'fulfilled' ? (Array.isArray(courses.value) ? courses.value.length : 0) : 0;
        const eCount = enrollments.status === 'fulfilled' ? (Array.isArray(enrollments.value) ? enrollments.value.length : 0) : 0;

        container.innerHTML = `
          ${this.statCard('📚', cCount, 'Mövcud Kurslar')}
          ${this.statCard('📋', eCount, 'Qeydiyyatlarım')}
        `;
      }
    } catch (err) {
      container.innerHTML = `<div class="stat-card"><p>Məlumat yüklənmədi.</p></div>`;
    }
  },

  statCard(icon, value, label) {
    return `<div class="stat-card">
      <div class="stat-icon">${icon}</div>
      <div class="stat-value">${value}</div>
      <div class="stat-label">${label}</div>
    </div>`;
  },
};

// === Courses ===
const Courses = {
  data: [],
  isSearching: false,

  async load() {
    this.isSearching = false;
    showLoading('courses-table-body');
    try {
      let data;
      if (App.role === 'ADMIN') {
        data = await API.courses.getAll();
      } else {
        data = await API.courses.getActive();
      }
      this.data = Array.isArray(data) ? data : [];
      this.render();
    } catch (err) {
      console.error('Course load error:', err);
      showEmpty('courses-table-body', '📚', 'Kurs yüklənmədi', err.message);
    }
  },

  render() {
    const tbody = document.getElementById('courses-table-body');
    if (!Array.isArray(this.data) || !this.data.length) {
      if (this.isSearching) {
        showEmpty('courses-table-body', '🔍', 'Nəticə tapılmadı', 'Axtarışınıza uyğun heç bir kurs tapılmadı.');
      } else {
        showEmpty('courses-table-body', '📚', 'Kurs yoxdur', 'Hələ heç bir kurs əlavə edilməyib.');
      }
      return;
    }

    tbody.innerHTML = this.data.filter(Boolean).map(c => `<tr>
      <td><strong>${c.title}</strong></td>
      <td>${c.description ? (c.description.substring(0, 60) + (c.description.length > 60 ? '...' : '')) : '—'}</td>
      <td>${c.duration} saat</td>
      <td>${c.price ? c.price.toFixed(2) + ' ₼' : '0.00 ₼'}</td>
      <td>${statusBadge(c.isActive)}</td>
      <td>${formatDate(c.create_at)}</td>
      <td class="actions">
        ${App.role === 'ADMIN' ? `
          <button class="btn btn-sm btn-secondary" onclick="Courses.edit(${c.id})" title="Redaktə">✏️</button>
          <button class="btn btn-sm btn-danger" onclick="Courses.remove(${c.id})" title="Sil">🗑️</button>
        ` : ''}
        ${App.role === 'ADMIN' || App.role === 'TEACHER' ? `
          <button class="btn btn-sm btn-info" onclick="Courses.viewStudents(${c.id}, '${c.title?.replace(/'/g, "\\'")}')" title="Tələbələr">👥 Tələbələr</button>
        ` : ''}
        ${App.role === 'STUDENT' ? `
          <button class="btn btn-sm btn-success" onclick="Enrollments.enrollByCourseId(${c.id})">📋 Qeydiyyat</button>
        ` : ''}
      </td>
    </tr>`).join('');
  },

  _searchTimeout: null,
  async search(query) {
    if (this._searchTimeout) clearTimeout(this._searchTimeout);
    this._searchTimeout = setTimeout(async () => {
      const q = query.trim();
      if (!q) return this.load();

      this.isSearching = true;
      try {
        const data = await API.courses.search(q);
        this.data = Array.isArray(data) ? data : [];
        this.render();
      } catch (err) {
        console.error('Course search error:', err);
        showEmpty('courses-table-body', '🔍', 'Xəta baş verdi', err.message);
      }
    }, 400);
  },

  openCreate() {
    document.getElementById('course-form').reset();
    document.getElementById('course-modal-title').textContent = 'Yeni Kurs';
    document.getElementById('course-id').value = '';
    openModal('course-modal');
  },

  async edit(id) {
    try {
      const c = await API.courses.getById(id);
      document.getElementById('course-id').value = c.id;
      document.getElementById('course-title').value = c.title || '';
      document.getElementById('course-description').value = c.description || '';
      document.getElementById('course-duration').value = c.duration || '';
      document.getElementById('course-price').value = c.price || '';
      document.getElementById('course-modal-title').textContent = 'Kursu Redaktə Et';
      openModal('course-modal');
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async save() {
    const id = document.getElementById('course-id').value;
    const data = {
      title: document.getElementById('course-title').value,
      description: document.getElementById('course-description').value,
      duration: parseInt(document.getElementById('course-duration').value),
      price: parseFloat(document.getElementById('course-price').value),
    };

    try {
      if (id) {
        await API.courses.update(id, data);
        Toast.success('Kurs yeniləndi!');
      } else {
        await API.courses.create(data);
        Toast.success('Kurs yaradıldı!');
      }
      closeModal('course-modal');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async remove(id) {
    if (!confirm('Bu kursu silmək istədiyinizdən əminsiniz?')) return;
    try {
      await API.courses.delete(id);
      Toast.success('Kurs silindi!');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async viewStudents(courseId, courseTitle) {
    const titleEl = document.getElementById('course-students-title');
    if (titleEl) titleEl.textContent = `${courseTitle} — Tələbələr`;
    showLoading('course-students-body');
    openModal('course-students-modal');

    try {
      const data = await API.enrollments.getByCourse(courseId);
      const tbody = document.getElementById('course-students-body');
      
      if (!Array.isArray(data) || !data.length) {
        tbody.innerHTML = '<tr><td colspan="3" style="text-align:center; padding: 20px;">Bu kurs üçün hələ heç bir tələbə qeydiyyatdan keçməyib.</td></tr>';
        return;
      }

      tbody.innerHTML = data.map(e => `<tr>
        <td><strong>${e.studentName} ${e.studentSurname}</strong><br><small>${e.studentEmail || ''}</small></td>
        <td>${enrollmentBadge(e.status)}</td>
        <td>${formatDate(e.enrollmentDate)}</td>
      </tr>`).join('');
    } catch (err) {
      Toast.error(err.message);
      closeModal('course-students-modal');
    }
  },
};

// === Teachers ===
const Teachers = {
  data: [],
  isSearching: false,

  async load() {
    this.isSearching = false;
    showLoading('teachers-table-body');
    try {
      const data = await API.teachers.getAll();
      this.data = Array.isArray(data) ? data : [];
      this.render();
    } catch (err) {
      showEmpty('teachers-table-body', '👨‍🏫', 'Müəllim yüklənmədi', err.message);
    }
  },

  render() {
    const tbody = document.getElementById('teachers-table-body');
    if (!this.data.length) {
      if (this.isSearching) {
        showEmpty('teachers-table-body', '🔍', 'Nəticə tapılmadı', 'Axtarışınıza uyğun müəllim tapılmadı.');
      } else {
        showEmpty('teachers-table-body', '👨‍🏫', 'Müəllim yoxdur', 'Hələ heç bir müəllim əlavə edilməyib.');
      }
      return;
    }

    tbody.innerHTML = this.data.map(t => `<tr>
      <td><strong>${t.name} ${t.surname}</strong></td>
      <td>${t.email}</td>
      <td>${t.phone || '—'}</td>
      <td>${t.age || '—'}</td>
      <td>${t.courseName || '—'}</td>
      <td>${statusBadge(t.isActive)}</td>
      <td class="actions">
        <button class="btn btn-sm btn-secondary" onclick="Teachers.edit(${t.id})" title="Redaktə">✏️</button>
        <button class="btn btn-sm btn-danger" onclick="Teachers.remove(${t.id})" title="Sil">🗑️</button>
      </td>
    </tr>`).join('');
  },

  _searchTimeout: null,
  async search(query) {
    if (this._searchTimeout) clearTimeout(this._searchTimeout);
    this._searchTimeout = setTimeout(async () => {
      const q = query.trim();
      if (!q) return this.load();

      this.isSearching = true;
      try {
        const data = await API.teachers.searchByName(q);
        this.data = Array.isArray(data) ? data : [];
        this.render();
      } catch (err) {
        console.error('Teacher search error:', err);
        showEmpty('teachers-table-body', '🔍', 'Xəta baş verdi', err.message);
      }
    }, 400);
  },

  async openCreate() {
    document.getElementById('teacher-form').reset();
    document.getElementById('teacher-modal-title').textContent = 'Yeni Müəllim';
    document.getElementById('teacher-id').value = '';

    // Load courses for select
    try {
      const courses = await API.courses.getAll();
      const sel = document.getElementById('teacher-courseId');
      sel.innerHTML = '<option value="">Kurs Seçin</option>' +
        (Array.isArray(courses) ? courses.map(c => `<option value="${c.id}">${c.title}</option>`).join('') : '');
    } catch { /* ignore */ }

    openModal('teacher-modal');
  },

  async edit(id) {
    try {
      const t = await API.teachers.getById(id);
      document.getElementById('teacher-id').value = t.id;
      document.getElementById('teacher-name').value = t.name || '';
      document.getElementById('teacher-surname').value = t.surname || '';
      document.getElementById('teacher-email').value = t.email || '';
      document.getElementById('teacher-age').value = t.age || '';
      document.getElementById('teacher-phone').value = t.phone || '';
      document.getElementById('teacher-modal-title').textContent = 'Müəllimi Redaktə Et';

      // Load courses
      try {
        const courses = await API.courses.getAll();
        const sel = document.getElementById('teacher-courseId');
        sel.innerHTML = '<option value="">Kurs Seçin</option>' +
          (Array.isArray(courses) ? courses.map(c => `<option value="${c.id}">${c.title}</option>`).join('') : '');
      } catch { /* ignore */ }

      openModal('teacher-modal');
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async save() {
    const id = document.getElementById('teacher-id').value;
    const data = {
      firstName: document.getElementById('teacher-name').value,
      lastName: document.getElementById('teacher-surname').value,
      email: document.getElementById('teacher-email').value,
      password: document.getElementById('teacher-password').value,
      age: parseInt(document.getElementById('teacher-age').value) || null,
      phone: document.getElementById('teacher-phone').value || null,
      courseId: parseInt(document.getElementById('teacher-courseId').value) || null,
    };

    try {
      if (id) {
        // Update via teacher endpoint
        await API.teachers.update(id, {
          name: data.firstName,
          surname: data.lastName,
          email: data.email,
          age: data.age,
          phone: data.phone,
          courseId: data.courseId,
        });
        Toast.success('Müəllim yeniləndi!');
      } else {
        // Create via admin endpoint — creates User + Teacher
        await API.admin.createTeacher(data);
        Toast.success('Müəllim yaradıldı!');
      }
      closeModal('teacher-modal');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async remove(id) {
    if (!confirm('Bu müəllimi silmək istədiyinizdən əminsiniz?')) return;
    try {
      await API.teachers.delete(id);
      Toast.success('Müəllim silindi!');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },
};

// === Students ===
const Students = {
  data: [],

  async load() {
    showLoading('students-table-body');
    try {
      const data = await API.students.getAll();
      this.data = Array.isArray(data) ? data : [];
      this.render();
    } catch (err) {
      showEmpty('students-table-body', '🎓', 'Tələbə tapılmadı', err.message);
    }
  },

  render() {
    const tbody = document.getElementById('students-table-body');
    if (!this.data.length) {
      showEmpty('students-table-body', '🎓', 'Tələbə yoxdur', 'Hələ heç bir tələbə əlavə edilməyib.');
      return;
    }

    tbody.innerHTML = this.data.map(s => `<tr>
      <td><strong>${s.name} ${s.surname}</strong></td>
      <td>${s.email}</td>
      <td>${s.phone || '—'}</td>
      <td>${s.age || '—'}</td>
      <td>${statusBadge(s.isActive)}</td>
      <td>${formatDate(s.create_at)}</td>
      <td class="actions">
        <button class="btn btn-sm btn-secondary" onclick="Students.edit(${s.id})" title="Redaktə">✏️</button>
        <button class="btn btn-sm btn-danger" onclick="Students.remove(${s.id})" title="Sil">🗑️</button>
      </td>
    </tr>`).join('');
  },

  openCreate() {
    document.getElementById('student-form').reset();
    document.getElementById('student-modal-title').textContent = 'Yeni Tələbə';
    document.getElementById('student-id').value = '';
    openModal('student-modal');
  },

  async edit(id) {
    try {
      const s = await API.students.getById(id);
      document.getElementById('student-id').value = s.id;
      document.getElementById('student-name').value = s.name || '';
      document.getElementById('student-surname').value = s.surname || '';
      document.getElementById('student-email').value = s.email || '';
      document.getElementById('student-age').value = s.age || '';
      document.getElementById('student-phone').value = s.phone || '';
      document.getElementById('student-modal-title').textContent = 'Tələbəni Redaktə Et';
      openModal('student-modal');
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async save() {
    const id = document.getElementById('student-id').value;
    const data = {
      name: document.getElementById('student-name').value,
      surname: document.getElementById('student-surname').value,
      email: document.getElementById('student-email').value,
      age: parseInt(document.getElementById('student-age').value),
      phone: document.getElementById('student-phone').value || null,
    };

    try {
      if (id) {
        await API.students.update(id, data);
        Toast.success('Tələbə yeniləndi!');
      } else {
        await API.students.create(data);
        Toast.success('Tələbə yaradıldı!');
      }
      closeModal('student-modal');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async remove(id) {
    if (!confirm('Bu tələbəni silmək istədiyinizdən əminsiniz?')) return;
    try {
      await API.students.delete(id);
      Toast.success('Tələbə silindi!');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },
};

// === Lessons ===
const Lessons = {
  data: [],
  isSearching: false,

  async load() {
    this.isSearching = false;
    showLoading('lessons-table-body');
    try {
      let data;
      if (App.role === 'ADMIN') {
        data = await API.lessons.getAll();
      } else {
        data = await API.lessons.getActive();
      }
      this.data = Array.isArray(data) ? data : [];
      this.render();
    } catch (err) {
      showEmpty('lessons-table-body', '📖', 'Dərs yüklənmədi', err.message);
    }
  },

  render() {
    const tbody = document.getElementById('lessons-table-body');
    if (!this.data.length) {
      if (this.isSearching) {
        showEmpty('lessons-table-body', '🔍', 'Nəticə tapılmadı', 'Axtarışınıza uyğun heç bir dərs tapılmadı.');
      } else {
        showEmpty('lessons-table-body', '📖', 'Dərs yoxdur', 'Hələ heç bir dərs əlavə edilməyib.');
      }
      return;
    }

    tbody.innerHTML = this.data.map(l => `<tr>
      <td><strong>${l.title}</strong></td>
      <td>${l.courseName || '—'}</td>
      <td>${l.content ? l.content.substring(0, 60) + (l.content.length > 60 ? '...' : '') : '—'}</td>
      <td>${l.videoURL ? `<a href="${l.videoURL}" target="_blank" style="color:var(--accent-secondary)">🎬 Video</a>` : '—'}</td>
      <td>${statusBadge(l.isActive)}</td>
      <td class="actions">
        ${(App.role === 'ADMIN' || App.role === 'TEACHER') ? `
          <button class="btn btn-sm btn-secondary" onclick="Lessons.edit(${l.id})" title="Redaktə">✏️</button>
        ` : ''}
        ${App.role === 'ADMIN' ? `
          <button class="btn btn-sm btn-danger" onclick="Lessons.remove(${l.id})" title="Sil">🗑️</button>
        ` : ''}
      </td>
    </tr>`).join('');
  },

  _searchTimeout: null,
  async search(query) {
    if (this._searchTimeout) clearTimeout(this._searchTimeout);
    this._searchTimeout = setTimeout(async () => {
      const q = query.trim();
      if (!q) return this.load();

      this.isSearching = true;
      try {
        const data = await API.lessons.searchByTitle(q);
        this.data = Array.isArray(data) ? data : [];
        this.render();
      } catch (err) {
        console.error('Lesson search error:', err);
        showEmpty('lessons-table-body', '🔍', 'Xəta baş verdi', err.message);
      }
    }, 400);
  },

  async openCreate() {
    document.getElementById('lesson-form').reset();
    document.getElementById('lesson-modal-title').textContent = 'Yeni Dərs';
    document.getElementById('lesson-id').value = '';

    // Load courses for select
    try {
      let courses;
      const selGroup = document.getElementById('lesson-courseId').closest('.form-group');
      if (App.role === 'ADMIN') {
        courses = await API.courses.getAll();
        selGroup.style.display = 'block';
      } else {
        courses = await API.courses.getActive();
        selGroup.style.display = 'none';
      }
      const sel = document.getElementById('lesson-courseId');
      sel.innerHTML = '<option value="">Kurs Seçin</option>' +
        (Array.isArray(courses) ? courses.map(c => `<option value="${c.id}">${c.title}</option>`).join('') : '');
    } catch { /* ignore */ }

    openModal('lesson-modal');
  },

  async edit(id) {
    try {
      const l = await API.lessons.getById(id);
      document.getElementById('lesson-id').value = l.id;
      document.getElementById('lesson-title').value = l.title || '';
      document.getElementById('lesson-content').value = l.content || '';
      document.getElementById('lesson-videoURL').value = l.videoURL || '';
      document.getElementById('lesson-modal-title').textContent = 'Dərsi Redaktə Et';

      // Load courses
      try {
        let courses;
        const selGroup = document.getElementById('lesson-courseId').closest('.form-group');
        if (App.role === 'ADMIN') {
          courses = await API.courses.getAll();
          selGroup.style.display = 'block';
        } else {
          courses = await API.courses.getActive();
          selGroup.style.display = 'none';
        }
        const sel = document.getElementById('lesson-courseId');
        sel.innerHTML = '<option value="">Kurs Seçin</option>' +
          (Array.isArray(courses) ? courses.map(c => `<option value="${c.id}">${c.title}</option>`).join('') : '');
      } catch { /* ignore */ }

      openModal('lesson-modal');
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async save() {
    const id = document.getElementById('lesson-id').value;
    const data = {
      title: document.getElementById('lesson-title').value,
      content: document.getElementById('lesson-content').value || null,
      videoURL: document.getElementById('lesson-videoURL').value || null,
      courseId: App.role === 'ADMIN' ? parseInt(document.getElementById('lesson-courseId').value) : null,
    };

    if (App.role === 'ADMIN' && !data.courseId) {
      Toast.error('Zəhmət olmasa kurs seçin.');
      return;
    }

    try {
      if (id) {
        await API.lessons.update(id, data);
        Toast.success('Dərs yeniləndi!');
      } else {
        await API.lessons.create(data);
        Toast.success('Dərs yaradıldı!');
      }
      closeModal('lesson-modal');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async remove(id) {
    if (!confirm('Bu dərsi silmək istədiyinizdən əminsiniz?')) return;
    try {
      await API.lessons.delete(id);
      Toast.success('Dərs silindi!');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async viewByCourse(courseId, courseTitle) {
    document.getElementById('lessons-view-title').textContent = `${courseTitle} — Dərslər`;
    showLoading('lessons-view-body');
    openModal('lessons-view-modal');

    try {
      const data = await API.lessons.getByCourse(courseId);
      const tbody = document.getElementById('lessons-view-body');
      
      if (!Array.isArray(data) || !data.length) {
        tbody.innerHTML = '<tr><td colspan="3" style="text-align:center; padding: 20px;">Bu kurs üçün hələ dərs əlavə edilməyib.</td></tr>';
        return;
      }

      tbody.innerHTML = data.map(l => `<tr>
        <td><strong>${l.title}</strong></td>
        <td>${l.content ? (l.content.substring(0, 50) + (l.content.length > 50 ? '...' : '')) : '—'}</td>
        <td>${l.videoURL ? `<a href="${l.videoURL}" target="_blank" class="btn btn-sm btn-primary">🎬 İzlə</a>` : '—'}</td>
      </tr>`).join('');
    } catch (err) {
      Toast.error(err.message);
      closeModal('lessons-view-modal');
    }
  },
};

// === Enrollments ===
const Enrollments = {
  data: [],

  async load() {
    showLoading('enrollments-table-body');
    try {
      const data = await API.enrollments.getAll();
      this.data = Array.isArray(data) ? data : [];
      this.render();
    } catch (err) {
      showEmpty('enrollments-table-body', '📋', 'Qeydiyyat tapılmadı', err.message);
    }
  },

  render() {
    const tbody = document.getElementById('enrollments-table-body');
    if (!this.data.length) {
      showEmpty('enrollments-table-body', '📋', 'Qeydiyyat yoxdur', 'Hələ heç bir qeydiyyat edilməyib.');
      return;
    }

    tbody.innerHTML = this.data.map(e => `<tr>
      <td>${e.studentName || ''} ${e.studentSurname || ''}</td>
      <td><strong>${e.courseTitle || '—'}</strong></td>
      <td>${formatDate(e.enrollmentDate)}</td>
      <td>${enrollmentBadge(e.status)}</td>
      <td>${statusBadge(e.isActive)}</td>
      <td class="actions">
        ${App.role === 'ADMIN' ? `
          ${e.status !== 'COMPLETED' ? `<button class="btn btn-sm btn-success" onclick="Enrollments.complete(${e.id})" title="Tamamla">✅</button>` : ''}
          <button class="btn btn-sm btn-danger" onclick="Enrollments.cancel(${e.id})" title="Ləğv et">❌</button>
        ` : ''}
        ${App.role === 'STUDENT' && e.status !== 'CANCELLED' ? `
          ${e.status === 'PENDING' ? `<button class="btn btn-sm btn-success" onclick="Enrollments.pay(${e.id})" title="Ödəniş et">💳 Ödə</button>` : ''}
          ${e.status === 'ACTIVE' ? `<button class="btn btn-sm btn-primary" onclick="Lessons.viewByCourse(${e.courseId}, '${e.courseTitle?.replace(/'/g, "\\'")}')" title="Dərslərə Bax">📖 Dərslər</button>` : ''}
          <button class="btn btn-sm btn-danger" onclick="Enrollments.cancel(${e.id})" title="Ləğv et">❌ Ləğv</button>
        ` : ''}
      </td>
    </tr>`).join('');
  },

  async openCreate() {
    document.getElementById('enrollment-form').reset();
    const sel = document.getElementById('enrollment-courseName');
    sel.innerHTML = '<option value="">Yüklənir...</option>';

    // Load active courses
    try {
      const courses = await API.courses.getActive();
      console.log('Available courses for enrollment:', courses);
      if (Array.isArray(courses) && courses.length > 0) {
        sel.innerHTML = '<option value="">Kurs Seçin</option>' +
          courses.map(c => `<option value="${c.title}">${c.title} — ${c.price ? c.price.toFixed(2) + ' ₼' : '0.00 ₼'}</option>`).join('');
      } else {
        sel.innerHTML = '<option value="">Aktiv kurs tapılmadı</option>';
      }
    } catch (err) {
      console.error('Enrollment modal load error:', err);
      sel.innerHTML = '<option value="">Kursları yükləmək mümkün olmadı</option>';
    }

    openModal('enrollment-modal');
  },

  async enrollByCourseId(courseId) {
    try {
      await API.enrollments.enroll({ courseId });
      Toast.success('Kursa uğurla qeydiyyat olundunuz!');
      if (App.currentSection === 'enrollments') this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async saveEnrollment() {
    const courseName = document.getElementById('enrollment-courseName').value;
    try {
      await API.enrollments.enroll({ courseName });
      Toast.success('Kursa uğurla qeydiyyat olundunuz!');
      closeModal('enrollment-modal');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async complete(id) {
    try {
      await API.enrollments.complete(id);
      Toast.success('Qeydiyyat tamamlandı!');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async cancel(id) {
    if (!confirm('Bu qeydiyyatı ləğv etmək istədiyinizdən əminsiniz?')) return;
    try {
      await API.enrollments.cancel(id);
      Toast.success('Qeydiyyat ləğv edildi!');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },

  async pay(id) {
    if (!confirm('Kurs ödənişini simulyasiya etmək istəyirsiniz?')) return;
    try {
      await API.enrollments.pay(id);
      Toast.success('Ödəniş uğurla tamamlandı! İndi dərslərə baxa bilərsiniz.');
      this.load();
    } catch (err) {
      Toast.error(err.message);
    }
  },
};

// === Init ===
document.addEventListener('DOMContentLoaded', () => App.init());
