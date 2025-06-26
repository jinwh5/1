/**
 * 公路施工人员管理系统通用工具函数
 */

// 工具函数命名空间
const Highway = {
    /**
     * 表单相关工具
     */
    Form: {
        /**
         * 初始化表单验证
         * @param {string} formSelector - 表单选择器
         * @param {Object} rules - 验证规则
         * @param {Function} submitCallback - 提交回调
         */
        initValidation: function(formSelector, rules, submitCallback) {
            const form = document.querySelector(formSelector);
            if (!form) return;
            
            // 表单提交事件
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                
                // 验证表单
                if (Highway.Form.validate(form, rules)) {
                    // 验证通过，调用回调
                    if (typeof submitCallback === 'function') {
                        submitCallback(Highway.Form.getFormData(form));
                    }
                }
            });
            
            // 实时验证
            form.querySelectorAll('input, select, textarea').forEach(function(field) {
                field.addEventListener('blur', function() {
                    Highway.Form.validateField(field, rules);
                });
                
                // 对于选择框，添加change事件
                if (field.tagName === 'SELECT') {
                    field.addEventListener('change', function() {
                        Highway.Form.validateField(field, rules);
                    });
                }
            });
        },
        
        /**
         * 验证整个表单
         * @param {HTMLFormElement} form - 表单元素
         * @param {Object} rules - 验证规则
         * @returns {boolean} 是否验证通过
         */
        validate: function(form, rules) {
            let isValid = true;
            
            // 清除所有错误
            form.querySelectorAll('.is-invalid').forEach(function(el) {
                el.classList.remove('is-invalid');
            });
            form.querySelectorAll('.invalid-feedback').forEach(function(el) {
                el.remove();
            });
            
            // 验证每个字段
            Object.keys(rules).forEach(function(fieldName) {
                const field = form.querySelector(`[name="${fieldName}"]`);
                if (field) {
                    if (!Highway.Form.validateField(field, rules)) {
                        isValid = false;
                    }
                }
            });
            
            return isValid;
        },
        
        /**
         * 验证单个字段
         * @param {HTMLElement} field - 表单字段
         * @param {Object} rules - 验证规则
         * @returns {boolean} 是否验证通过
         */
        validateField: function(field, rules) {
            const fieldName = field.getAttribute('name');
            if (!fieldName || !rules[fieldName]) return true;
            
            const fieldRules = rules[fieldName];
            const value = field.value.trim();
            let isValid = true;
            let errorMessage = '';
            
            // 检查必填项
            if (fieldRules.required && value === '') {
                isValid = false;
                errorMessage = fieldRules.messages?.required || '此字段是必填的';
            }
            // 检查最小长度
            else if (fieldRules.minLength && value.length < fieldRules.minLength) {
                isValid = false;
                errorMessage = fieldRules.messages?.minLength || `最少需要 ${fieldRules.minLength} 个字符`;
            }
            // 检查最大长度
            else if (fieldRules.maxLength && value.length > fieldRules.maxLength) {
                isValid = false;
                errorMessage = fieldRules.messages?.maxLength || `最多允许 ${fieldRules.maxLength} 个字符`;
            }
            // 检查正则表达式
            else if (fieldRules.pattern && !new RegExp(fieldRules.pattern).test(value)) {
                isValid = false;
                errorMessage = fieldRules.messages?.pattern || '格式不正确';
            }
            // 自定义验证函数
            else if (typeof fieldRules.validator === 'function') {
                const result = fieldRules.validator(value, field);
                if (result !== true) {
                    isValid = false;
                    errorMessage = result || '验证失败';
                }
            }
            
            // 显示错误信息
            if (!isValid) {
                field.classList.add('is-invalid');
                
                // 检查是否已存在错误信息
                let feedback = field.nextElementSibling;
                if (!feedback || !feedback.classList.contains('invalid-feedback')) {
                    feedback = document.createElement('div');
                    feedback.className = 'invalid-feedback';
                    field.parentNode.insertBefore(feedback, field.nextSibling);
                }
                feedback.textContent = errorMessage;
            } else {
                field.classList.remove('is-invalid');
                const feedback = field.nextElementSibling;
                if (feedback && feedback.classList.contains('invalid-feedback')) {
                    feedback.remove();
                }
            }
            
            return isValid;
        },
        
        /**
         * 获取表单数据
         * @param {HTMLFormElement} form - 表单元素
         * @returns {Object} 表单数据对象
         */
        getFormData: function(form) {
            const formData = new FormData(form);
            const data = {};
            
            for (const [key, value] of formData.entries()) {
                // 处理复选框
                if (form.querySelectorAll(`[name="${key}"]`).length > 1) {
                    if (!data[key]) {
                        data[key] = [];
                    }
                    data[key].push(value);
                } else {
                    data[key] = value;
                }
            }
            
            return data;
        },
        
        /**
         * 填充表单数据
         * @param {HTMLFormElement} form - 表单元素
         * @param {Object} data - 表单数据
         */
        fillFormData: function(form, data) {
            Object.keys(data).forEach(function(key) {
                const field = form.querySelector(`[name="${key}"]`);
                if (field) {
                    if (field.type === 'checkbox' || field.type === 'radio') {
                        field.checked = !!data[key];
                    } else if (field.tagName === 'SELECT') {
                        field.value = data[key];
                    } else {
                        field.value = data[key];
                    }
                }
            });
        }
    },
    
    /**
     * HTTP请求工具
     */
    Http: {
        /**
         * 发送GET请求
         * @param {string} url - 请求URL
         * @param {Object} params - 请求参数
         * @returns {Promise} Promise对象
         */
        get: function(url, params = {}) {
            // 构建查询字符串
            const queryString = Object.keys(params)
                .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
                .join('&');
            
            const fullUrl = queryString ? `${url}?${queryString}` : url;
            
            return fetch(fullUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                credentials: 'same-origin'
            })
            .then(Highway.Http.handleResponse);
        },
        
        /**
         * 发送POST请求
         * @param {string} url - 请求URL
         * @param {Object} data - 请求数据
         * @returns {Promise} Promise对象
         */
        post: function(url, data = {}) {
            return fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                credentials: 'same-origin',
                body: JSON.stringify(data)
            })
            .then(Highway.Http.handleResponse);
        },
        
        /**
         * 处理响应
         * @param {Response} response - Fetch API响应对象
         * @returns {Promise} Promise对象
         */
        handleResponse: function(response) {
            // 检查HTTP状态
            if (!response.ok) {
                if (response.status === 401) {
                    // 未授权，重定向到登录页面
                    window.location.href = '/login';
                    return Promise.reject('未授权，请重新登录');
                }
                
                if (response.status === 403) {
                    Highway.UI.showToast('无权限执行此操作', 'error');
                    return Promise.reject('权限不足');
                }
                
                return response.json()
                    .then(data => Promise.reject(data.message || `请求失败 (${response.status})`))
                    .catch(err => Promise.reject(`请求失败 (${response.status})`));
            }
            
            // 正常返回数据
            return response.json().then(data => {
                // 处理API返回的标准结构
                if (data.code !== undefined) {
                    if (data.code === 0) {
                        return data.data;
                    } else {
                        return Promise.reject(data.message || '操作失败');
                    }
                }
                
                return data;
            });
        }
    },
    
    /**
     * UI工具
     */
    UI: {
        /**
         * 显示提示消息
         * @param {string} message - 消息内容
         * @param {string} type - 消息类型: success, error, warning, info
         * @param {number} duration - 显示时间(毫秒)
         */
        showToast: function(message, type = 'info', duration = 3000) {
            const container = document.getElementById('toast-container');
            
            // 创建容器(如果不存在)
            if (!container) {
                const newContainer = document.createElement('div');
                newContainer.id = 'toast-container';
                newContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
                document.body.appendChild(newContainer);
            }
            
            // 创建Toast元素
            const toastId = 'toast-' + Date.now();
            const toast = document.createElement('div');
            toast.className = `toast-item toast-${type}`;
            toast.id = toastId;
            toast.innerHTML = `
                <div class="toast-header">
                    <strong class="me-auto">${type.charAt(0).toUpperCase() + type.slice(1)}</strong>
                    <button type="button" class="btn-close" aria-label="Close"></button>
                </div>
                <div class="toast-body">
                    ${message}
                </div>
            `;
            
            // 添加到容器
            document.getElementById('toast-container').appendChild(toast);
            
            // 关闭按钮事件
            toast.querySelector('.btn-close').addEventListener('click', function() {
                Highway.UI.hideToast(toastId);
            });
            
            // 显示Toast
            setTimeout(function() {
                toast.classList.add('show');
            }, 10);
            
            // 设置自动关闭
            if (duration > 0) {
                setTimeout(function() {
                    Highway.UI.hideToast(toastId);
                }, duration);
            }
        },
        
        /**
         * 隐藏提示消息
         * @param {string} id - Toast ID
         */
        hideToast: function(id) {
            const toast = document.getElementById(id);
            if (toast) {
                toast.classList.remove('show');
                
                // 动画结束后移除元素
                setTimeout(function() {
                    toast.remove();
                }, 300);
            }
        },
        
        /**
         * 显示确认对话框
         * @param {string} message - 消息内容
         * @param {Function} okCallback - 确认回调
         * @param {Function} cancelCallback - 取消回调
         * @param {Object} options - 配置选项
         */
        showConfirm: function(message, okCallback, cancelCallback, options = {}) {
            const defaults = {
                title: '确认',
                okText: '确认',
                cancelText: '取消',
                type: 'warning' // success, warning, error, info
            };
            
            const config = {...defaults, ...options};
            
            // 创建模态框
            const modalId = 'modal-confirm-' + Date.now();
            const modal = document.createElement('div');
            modal.className = 'modal fade';
            modal.id = modalId;
            modal.tabIndex = '-1';
            modal.setAttribute('aria-hidden', 'true');
            
            modal.innerHTML = `
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">${config.title}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="confirm-icon confirm-${config.type}">
                                <i class="fas fa-${config.type === 'warning' ? 'exclamation-triangle' : 
                                                config.type === 'success' ? 'check-circle' : 
                                                config.type === 'error' ? 'times-circle' : 'info-circle'}"></i>
                            </div>
                            <div class="confirm-message">${message}</div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">${config.cancelText}</button>
                            <button type="button" class="btn btn-${config.type === 'warning' ? 'warning' : 
                                                                config.type === 'success' ? 'success' : 
                                                                config.type === 'error' ? 'danger' : 'info'}" id="btn-confirm-ok">${config.okText}</button>
                        </div>
                    </div>
                </div>
            `;
            
            // 添加到body
            document.body.appendChild(modal);
            
            // 实例化Bootstrap模态框
            const modalInstance = new bootstrap.Modal(modal);
            modalInstance.show();
            
            // 确认按钮事件
            document.getElementById('btn-confirm-ok').addEventListener('click', function() {
                if (typeof okCallback === 'function') {
                    okCallback();
                }
                modalInstance.hide();
            });
            
            // 取消按钮事件
            modal.querySelector('.btn-secondary').addEventListener('click', function() {
                if (typeof cancelCallback === 'function') {
                    cancelCallback();
                }
            });
            
            // 模态框关闭后销毁
            modal.addEventListener('hidden.bs.modal', function() {
                modal.remove();
            });
        },
        
        /**
         * 显示加载中
         * @param {string} message - 加载消息
         * @returns {Object} 加载对象，包含close方法
         */
        showLoading: function(message = '加载中...') {
            const loadingId = 'loading-' + Date.now();
            const loading = document.createElement('div');
            loading.className = 'loading-overlay';
            loading.id = loadingId;
            
            loading.innerHTML = `
                <div class="loading-spinner">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">加载中...</span>
                    </div>
                    <div class="loading-text">${message}</div>
                </div>
            `;
            
            document.body.appendChild(loading);
            
            // 添加禁止滚动
            document.body.style.overflow = 'hidden';
            
            // 返回带有close方法的对象
            return {
                close: function() {
                    const loadingElement = document.getElementById(loadingId);
                    if (loadingElement) {
                        loadingElement.remove();
                        document.body.style.overflow = '';
                    }
                },
                update: function(newMessage) {
                    const loadingElement = document.getElementById(loadingId);
                    if (loadingElement) {
                        loadingElement.querySelector('.loading-text').textContent = newMessage;
                    }
                }
            };
        }
    },
    
    /**
     * 通用工具函数
     */
    Utils: {
        /**
         * 格式化日期
         * @param {Date|string|number} date - 日期对象、字符串或时间戳
         * @param {string} format - 格式化模板
         * @returns {string} 格式化后的日期字符串
         */
        formatDate: function(date, format = 'YYYY-MM-DD') {
            if (!date) return '';
            
            // 转换为Date对象
            const d = typeof date === 'object' ? date : new Date(date);
            
            // 检查是否有效日期
            if (isNaN(d.getTime())) return '';
            
            const year = d.getFullYear();
            const month = d.getMonth() + 1;
            const day = d.getDate();
            const hours = d.getHours();
            const minutes = d.getMinutes();
            const seconds = d.getSeconds();
            
            // 填充单个数字
            const pad = (num) => (num < 10 ? '0' + num : num);
            
            // 替换格式
            return format
                .replace('YYYY', year)
                .replace('YY', String(year).slice(-2))
                .replace('MM', pad(month))
                .replace('M', month)
                .replace('DD', pad(day))
                .replace('D', day)
                .replace('HH', pad(hours))
                .replace('H', hours)
                .replace('mm', pad(minutes))
                .replace('m', minutes)
                .replace('ss', pad(seconds))
                .replace('s', seconds);
        },
        
        /**
         * 节流函数
         * @param {Function} func - 要执行的函数
         * @param {number} delay - 延迟时间(毫秒)
         * @returns {Function} 节流后的函数
         */
        throttle: function(func, delay = 300) {
            let lastCall = 0;
            return function(...args) {
                const now = Date.now();
                if (now - lastCall >= delay) {
                    lastCall = now;
                    return func.apply(this, args);
                }
            };
        },
        
        /**
         * 防抖函数
         * @param {Function} func - 要执行的函数
         * @param {number} delay - 延迟时间(毫秒)
         * @returns {Function} 防抖后的函数
         */
        debounce: function(func, delay = 300) {
            let timer = null;
            return function(...args) {
                if (timer) clearTimeout(timer);
                timer = setTimeout(() => {
                    func.apply(this, args);
                }, delay);
            };
        },
        
        /**
         * 生成UUID
         * @returns {string} UUID字符串
         */
        generateUUID: function() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                const r = Math.random() * 16 | 0;
                const v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        },
        
        /**
         * 获取URL参数
         * @param {string} name - 参数名
         * @returns {string|null} 参数值
         */
        getUrlParam: function(name) {
            const urlParams = new URLSearchParams(window.location.search);
            return urlParams.get(name);
        }
    }
};

// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 初始化页面
    initPage();
    
    // 添加全局事件处理
    addGlobalEvents();
});

/**
 * 初始化页面
 */
function initPage() {
    // 初始化工具提示
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // 初始化下拉菜单
    const dropdownTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="dropdown"]'));
    dropdownTriggerList.map(function(dropdownTriggerEl) {
        return new bootstrap.Dropdown(dropdownTriggerEl);
    });
}

/**
 * 添加全局事件处理
 */
function addGlobalEvents() {
    // 处理数据确认按钮
    document.addEventListener('click', function(e) {
        if (e.target.matches('[data-confirm]')) {
            e.preventDefault();
            
            const message = e.target.getAttribute('data-confirm') || '确定要执行此操作吗？';
            const url = e.target.getAttribute('href') || e.target.getAttribute('data-url');
            
            if (!url) return;
            
            Highway.UI.showConfirm(message, function() {
                window.location.href = url;
            });
        }
    });
    
    // 处理Ajax提交按钮
    document.addEventListener('click', function(e) {
        if (e.target.matches('[data-ajax]')) {
            e.preventDefault();
            
            const url = e.target.getAttribute('data-ajax');
            const method = (e.target.getAttribute('data-method') || 'GET').toUpperCase();
            const confirm = e.target.getAttribute('data-confirm');
            
            if (!url) return;
            
            const submitRequest = function() {
                const loading = Highway.UI.showLoading();
                
                if (method === 'GET') {
                    Highway.Http.get(url)
                        .then(function(data) {
                            loading.close();
                            Highway.UI.showToast(data.message || '操作成功', 'success');
                            
                            // 检查是否需要刷新页面
                            if (e.target.getAttribute('data-refresh') !== 'false') {
                                setTimeout(function() {
                                    window.location.reload();
                                }, 1000);
                            }
                        })
                        .catch(function(error) {
                            loading.close();
                            Highway.UI.showToast(error || '操作失败', 'error');
                        });
                } else {
                    Highway.Http.post(url)
                        .then(function(data) {
                            loading.close();
                            Highway.UI.showToast(data.message || '操作成功', 'success');
                            
                            // 检查是否需要刷新页面
                            if (e.target.getAttribute('data-refresh') !== 'false') {
                                setTimeout(function() {
                                    window.location.reload();
                                }, 1000);
                            }
                        })
                        .catch(function(error) {
                            loading.close();
                            Highway.UI.showToast(error || '操作失败', 'error');
                        });
                }
            };
            
            if (confirm) {
                Highway.UI.showConfirm(confirm, submitRequest);
            } else {
                submitRequest();
            }
        }
    });
}

// 导出全局对象
window.Highway = Highway; 