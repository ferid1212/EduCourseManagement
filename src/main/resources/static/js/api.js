// === EduCourse Management System — API Service Layer ===

const API = {
  // Automatically detect base URL for local development
  // Prioritize 8081 as per user's application.yaml
  BASE: window.location.port === '5500' ? 'http://localhost:8081' : '',

  getToken() {
    return localStorage.getItem('edu_token');
  },

  setToken(token) {
    localStorage.setItem('edu_token', token);
  },

  removeToken() {
    localStorage.removeItem('edu_token');
  },

  decodeToken(token) {
    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch (e) {
      console.error('Token decoding error:', e);
      return null;
    }
  },

  getUserRole() {
    const token = this.getToken();
    if (!token) return null;
    const decoded = this.decodeToken(token);
    if (!decoded) return null;
    return decoded.role ? decoded.role.replace('ROLE_', '') : null;
  },

  getUserEmail() {
    const token = this.getToken();
    if (!token) return null;
    const decoded = this.decodeToken(token);
    return decoded ? decoded.sub : null;
  },

  isTokenExpired() {
    const token = this.getToken();
    if (!token) return true;
    const decoded = this.decodeToken(token);
    if (!decoded || !decoded.exp) return true;
    return decoded.exp * 1000 < Date.now();
  },

  async request(url, options = {}) {
    const token = this.getToken();
    const headers = {
      'Content-Type': 'application/json',
      ...options.headers,
    };
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    try {
      console.log(`API Request: ${options.method || 'GET'} ${this.BASE}${url}`);
      const response = await fetch(`${this.BASE}${url}`, {
        ...options,
        headers,
      });

      if (response.status === 401 || response.status === 403) {
        if (response.status === 401) {
          console.warn('Unauthorized request. Clearing token.');
          this.removeToken();
          window.location.reload();
          throw new Error('Oturum müddəti bitib. Yenidən daxil olun.');
        }
        throw new Error('Bu əməliyyat üçün icazəniz yoxdur.');
      }

      const text = await response.text();
      let data;
      try {
        data = text ? JSON.parse(text) : null;
      } catch {
        data = text;
      }

      if (!response.ok) {
        throw new Error(typeof data === 'string' ? data : (data && data.message) || 'Xəta baş verdi');
      }

      return data;
    } catch (error) {
      console.error('API Error:', error);
      if (error.message === 'Failed to fetch') {
        throw new Error('Server ilə əlaqə qurulmadı. Serverin (8080 portu) işlədiyindən əmin olun.');
      }
      throw error;
    }
  },

  // === Auth ===
  auth: {
    async login(email, password) {
      return API.request('/api/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      });
    },
    async register(data) {
      return API.request('/api/auth/register', {
        method: 'POST',
        body: JSON.stringify(data),
      });
    },
  },

  // === Admin ===
  admin: {
    async createTeacher(data) {
      return API.request('/api/admin/create-teacher', {
        method: 'POST',
        body: JSON.stringify(data),
      });
    },
  },

  // === Courses ===
  courses: {
    async getAll() {
      return API.request('/courses');
    },
    async getById(id) {
      return API.request(`/courses/${id}`);
    },
    async getActive() {
      return API.request('/courses/active');
    },
    async search(title) {
      return API.request(`/courses/search/${encodeURIComponent(title)}`);
    },
    async getByTeacher(teacherId) {
      return API.request(`/courses/teacher/${teacherId}`);
    },
    async create(data) {
      return API.request('/courses', {
        method: 'POST',
        body: JSON.stringify(data),
      });
    },
    async update(id, data) {
      return API.request(`/courses/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data),
      });
    },
    async delete(id) {
      return API.request(`/courses/${id}`, { method: 'DELETE' });
    },
    async hardDelete(id) {
      return API.request(`/courses/hardDelete/${id}`, { method: 'DELETE' });
    },
  },

  // === Teachers ===
  teachers: {
    async getAll() {
      return API.request('/teachers');
    },
    async getById(id) {
      return API.request(`/teachers/${id}`);
    },
    async getActive() {
      return API.request('/teachers/active');
    },
    async getByEmail(email) {
      return API.request(`/teachers/email/${encodeURIComponent(email)}`);
    },
    async searchByName(name) {
      return API.request(`/teachers/name/${encodeURIComponent(name)}`);
    },
    async create(data) {
      return API.request('/teachers', {
        method: 'POST',
        body: JSON.stringify(data),
      });
    },
    async update(id, data) {
      return API.request(`/teachers/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data),
      });
    },
    async delete(id) {
      return API.request(`/teachers/${id}`, { method: 'DELETE' });
    },
    async hardDelete(id) {
      return API.request(`/teachers/delete/${id}`, { method: 'DELETE' });
    },
  },

  // === Students ===
  students: {
    async getAll() {
      return API.request('/students');
    },
    async getById(id) {
      return API.request(`/students/${id}`);
    },
    async getActive() {
      return API.request('/students/active');
    },
    async create(data) {
      return API.request('/students', {
        method: 'POST',
        body: JSON.stringify(data),
      });
    },
    async update(id, data) {
      return API.request(`/students/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data),
      });
    },
    async delete(id) {
      return API.request(`/students/${id}`, { method: 'DELETE' });
    },
    async hardDelete(id) {
      return API.request(`/students/hard/${id}`, { method: 'DELETE' });
    },
  },

  // === Lessons ===
  lessons: {
    async getAll() {
      return API.request('/lessons');
    },
    async getById(id) {
      return API.request(`/lessons/${id}`);
    },
    async getActive() {
      return API.request('/lessons/active');
    },
    async searchByTitle(title) {
      return API.request(`/lessons/title/${encodeURIComponent(title)}`);
    },
    async create(data) {
      return API.request('/lessons', {
        method: 'POST',
        body: JSON.stringify(data),
      });
    },
    async update(id, data) {
      return API.request(`/lessons/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data),
      });
    },
    async updateVideoUrl(id, videoURL) {
      return API.request(`/lessons/video/${id}/${encodeURIComponent(videoURL)}`, {
        method: 'PUT',
      });
    },
    async delete(id) {
      return API.request(`/lessons/${id}`, { method: 'DELETE' });
    },
    async hardDelete(id) {
      return API.request(`/lessons/hard/${id}`, { method: 'DELETE' });
    },
  },

  // === Enrollments ===
  enrollments: {
    async getAll() {
      return API.request('/enrollments');
    },
    async getById(id) {
      return API.request(`/enrollments/${id}`);
    },
    async getByStudent(studentId) {
      return API.request(`/enrollments/student/${studentId}`);
    },
    async getByCourse(courseId) {
      return API.request(`/enrollments/course/${courseId}`);
    },
    async getByStatus(status) {
      return API.request(`/enrollments/status/${status}`);
    },
    async getActiveCount(courseId) {
      return API.request(`/enrollments/course/${courseId}/count`);
    },
    async checkEnrollment(studentId, courseId) {
      return API.request(`/enrollments/check?studentId=${studentId}&courseId=${courseId}`);
    },
    async enroll(data) {
      return API.request('/enrollments', {
        method: 'POST',
        body: JSON.stringify(data),
      });
    },
    async updateStatus(id, status) {
      return API.request(`/enrollments/${id}/status?status=${status}`, {
        method: 'PUT',
      });
    },
    async complete(id) {
      return API.request(`/enrollments/${id}/complete`, { method: 'PUT' });
    },
    async cancel(id) {
      return API.request(`/enrollments/${id}`, { method: 'DELETE' });
    },
    async hardDelete(id) {
      return API.request(`/enrollments/hard/${id}`, { method: 'DELETE' });
    },
  },
};
