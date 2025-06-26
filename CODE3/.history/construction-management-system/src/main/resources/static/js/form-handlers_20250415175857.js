/**
 * 表单处理相关功能模块
 * 用于处理各种表单的提交和验证
 */

// 在文档加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 初始化所有表单处理
    initializeFormHandlers();
});

/**
 * 初始化表单处理程序
 */
function initializeFormHandlers() {
    // 初始化工人信息表单
    initializeWorkerForm();
    
    // 初始化考勤记录表单
    initializeAttendanceForm();
    
    // 初始化安全培训表单
    initializeSafetyTrainingForm();
    
    // 初始化项目进度表单
    initializeProjectProgressForm();
    
    // 初始化资质证书表单
    initializeQualificationForm();
}

/**
 * 初始化工人信息表单
 */
function initializeWorkerForm() {
    const workerForm = document.getElementById('workerForm');
    if (!workerForm) return;
    
    // 为表单添加提交事件监听器
    workerForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // 验证表单
        if (!validateWorkerForm(workerForm)) {
            return;
        }
        
        // 获取表单数据
        const formData = new FormData(workerForm);
        const jsonData = formDataToJson(formData);
        
        // 确定请求URL和方法
        const workerId = workerForm.getAttribute('data-worker-id');
        const url = workerId ? `/api/workers/${workerId}` : '/api/workers';
        const method = workerId ? 'PUT' : 'POST';
        
        // 发送AJAX请求
        sendAjaxRequest(
            url,
            method,
            jsonData,
            function(response) {
                // 成功处理
                showSuccessMessage('工人信息保存成功');
                
                // 如果是添加操作，重置表单
                if (!workerId) {
                    workerForm.reset();
                }
                
                // 如果设置了回调函数，调用它
                if (typeof workerFormCallback === 'function') {
                    workerFormCallback(response);
                }
            },
            function(status, error) {
                // 错误处理
                showErrorMessage('保存工人信息失败：' + (error.message || '未知错误'));
            }
        );
    });
    
    // 初始化身份证号验证
    const idCardInput = workerForm.querySelector('[name="idCard"]');
    if (idCardInput) {
        idCardInput.addEventListener('blur', function() {
            validateIdCard(idCardInput);
        });
    }
    
    // 初始化手机号验证
    const phoneInput = workerForm.querySelector('[name="phone"]');
    if (phoneInput) {
        phoneInput.addEventListener('blur', function() {
            validatePhone(phoneInput);
        });
    }
}

/**
 * 验证工人信息表单
 * @param {HTMLFormElement} form 表单元素
 * @returns {boolean} 验证结果
 */
function validateWorkerForm(form) {
    let isValid = true;
    
    // 验证姓名
    const nameInput = form.querySelector('[name="name"]');
    if (nameInput && !nameInput.value.trim()) {
        showInputError(nameInput, '姓名不能为空');
        isValid = false;
    }
    
    // 验证身份证号
    const idCardInput = form.querySelector('[name="idCard"]');
    if (idCardInput && !validateIdCard(idCardInput)) {
        isValid = false;
    }
    
    // 验证手机号
    const phoneInput = form.querySelector('[name="phone"]');
    if (phoneInput && !validatePhone(phoneInput)) {
        isValid = false;
    }
    
    return isValid;
}

/**
 * 验证身份证号
 * @param {HTMLInputElement} input 输入框元素
 * @returns {boolean} 验证结果
 */
function validateIdCard(input) {
    const value = input.value.trim();
    
    // 身份证号可以为空
    if (!value) {
        return true;
    }
    
    // 身份证号正则表达式（18位）
    const idCardRegex = /(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    
    if (!idCardRegex.test(value)) {
        showInputError(input, '请输入有效的18位身份证号码');
        return false;
    }
    
    clearInputError(input);
    return true;
}

/**
 * 验证手机号
 * @param {HTMLInputElement} input 输入框元素
 * @returns {boolean} 验证结果
 */
function validatePhone(input) {
    const value = input.value.trim();
    
    // 手机号可以为空
    if (!value) {
        return true;
    }
    
    // 手机号正则表达式
    const phoneRegex = /^1[3-9]\d{9}$/;
    
    if (!phoneRegex.test(value)) {
        showInputError(input, '请输入有效的11位手机号码');
        return false;
    }
    
    clearInputError(input);
    return true;
}

/**
 * 初始化考勤记录表单
 */
function initializeAttendanceForm() {
    const attendanceForm = document.getElementById('attendanceForm');
    if (!attendanceForm) return;
    
    // 为表单添加提交事件监听器
    attendanceForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // 验证表单
        if (!validateAttendanceForm(attendanceForm)) {
            return;
        }
        
        // 获取表单数据
        const formData = new FormData(attendanceForm);
        const jsonData = formDataToJson(formData);
        
        // 确定请求URL和方法
        const attendanceId = attendanceForm.getAttribute('data-attendance-id');
        const url = attendanceId ? `/api/attendance/${attendanceId}` : '/api/attendance';
        const method = attendanceId ? 'PUT' : 'POST';
        
        // 发送AJAX请求
        sendAjaxRequest(
            url,
            method,
            jsonData,
            function(response) {
                // 成功处理
                showSuccessMessage('考勤记录保存成功');
                
                // 如果是添加操作，重置表单
                if (!attendanceId) {
                    attendanceForm.reset();
                }
                
                // 如果设置了回调函数，调用它
                if (typeof attendanceFormCallback === 'function') {
                    attendanceFormCallback(response);
                }
            },
            function(status, error) {
                // 错误处理
                showErrorMessage('保存考勤记录失败：' + (error.message || '未知错误'));
            }
        );
    });
}

/**
 * 验证考勤记录表单
 * @param {HTMLFormElement} form 表单元素
 * @returns {boolean} 验证结果
 */
function validateAttendanceForm(form) {
    let isValid = true;
    
    // 验证工人ID
    const workerIdInput = form.querySelector('[name="workerId"]');
    if (workerIdInput && !workerIdInput.value.trim()) {
        showInputError(workerIdInput, '请选择工人');
        isValid = false;
    }
    
    // 验证日期
    const dateInput = form.querySelector('[name="date"]');
    if (dateInput && !dateInput.value.trim()) {
        showInputError(dateInput, '请选择日期');
        isValid = false;
    }
    
    // 验证状态
    const statusInput = form.querySelector('[name="status"]');
    if (statusInput && !statusInput.value.trim()) {
        showInputError(statusInput, '请选择考勤状态');
        isValid = false;
    }
    
    return isValid;
}

/**
 * 初始化安全培训表单
 */
function initializeSafetyTrainingForm() {
    const safetyTrainingForm = document.getElementById('safetyTrainingForm');
    if (!safetyTrainingForm) return;
    
    // 为表单添加提交事件监听器
    safetyTrainingForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // 验证表单
        if (!validateSafetyTrainingForm(safetyTrainingForm)) {
            return;
        }
        
        // 获取表单数据
        const formData = new FormData(safetyTrainingForm);
        const jsonData = formDataToJson(formData);
        
        // 确定请求URL和方法
        const trainingId = safetyTrainingForm.getAttribute('data-training-id');
        const url = trainingId ? `/api/safety-trainings/${trainingId}` : '/api/safety-trainings';
        const method = trainingId ? 'PUT' : 'POST';
        
        // 发送AJAX请求
        sendAjaxRequest(
            url,
            method,
            jsonData,
            function(response) {
                // 成功处理
                showSuccessMessage('安全培训记录保存成功');
                
                // 如果是添加操作，重置表单
                if (!trainingId) {
                    safetyTrainingForm.reset();
                }
                
                // 如果设置了回调函数，调用它
                if (typeof safetyTrainingFormCallback === 'function') {
                    safetyTrainingFormCallback(response);
                }
            },
            function(status, error) {
                // 错误处理
                showErrorMessage('保存安全培训记录失败：' + (error.message || '未知错误'));
            }
        );
    });
}

/**
 * 验证安全培训表单
 * @param {HTMLFormElement} form 表单元素
 * @returns {boolean} 验证结果
 */
function validateSafetyTrainingForm(form) {
    let isValid = true;
    
    // 验证培训名称
    const titleInput = form.querySelector('[name="title"]');
    if (titleInput && !titleInput.value.trim()) {
        showInputError(titleInput, '培训名称不能为空');
        isValid = false;
    }
    
    // 验证培训日期
    const dateInput = form.querySelector('[name="trainingDate"]');
    if (dateInput && !dateInput.value.trim()) {
        showInputError(dateInput, '请选择培训日期');
        isValid = false;
    }
    
    return isValid;
}

/**
 * 初始化项目进度表单
 */
function initializeProjectProgressForm() {
    const progressForm = document.getElementById('progressForm');
    if (!progressForm) return;
    
    // 为表单添加提交事件监听器
    progressForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // 验证表单
        if (!validateProgressForm(progressForm)) {
            return;
        }
        
        // 获取表单数据
        const formData = new FormData(progressForm);
        const jsonData = formDataToJson(formData);
        
        // 确定请求URL和方法
        const progressId = progressForm.getAttribute('data-progress-id');
        const url = progressId ? `/api/progress/${progressId}` : '/api/progress';
        const method = progressId ? 'PUT' : 'POST';
        
        // 发送AJAX请求
        sendAjaxRequest(
            url,
            method,
            jsonData,
            function(response) {
                // 成功处理
                showSuccessMessage('项目进度保存成功');
                
                // 如果是添加操作，重置表单
                if (!progressId) {
                    progressForm.reset();
                }
                
                // 如果设置了回调函数，调用它
                if (typeof progressFormCallback === 'function') {
                    progressFormCallback(response);
                }
            },
            function(status, error) {
                // 错误处理
                showErrorMessage('保存项目进度失败：' + (error.message || '未知错误'));
            }
        );
    });
}

/**
 * 验证项目进度表单
 * @param {HTMLFormElement} form 表单元素
 * @returns {boolean} 验证结果
 */
function validateProgressForm(form) {
    let isValid = true;
    
    // 验证项目阶段
    const stageInput = form.querySelector('[name="stage"]');
    if (stageInput && !stageInput.value.trim()) {
        showInputError(stageInput, '项目阶段不能为空');
        isValid = false;
    }
    
    // 验证计划进度
    const plannedInput = form.querySelector('[name="plannedProgress"]');
    if (plannedInput) {
        const plannedValue = parseFloat(plannedInput.value);
        if (isNaN(plannedValue) || plannedValue < 0 || plannedValue > 100) {
            showInputError(plannedInput, '计划进度必须是0-100之间的数字');
            isValid = false;
        }
    }
    
    // 验证实际进度
    const actualInput = form.querySelector('[name="actualProgress"]');
    if (actualInput) {
        const actualValue = parseFloat(actualInput.value);
        if (isNaN(actualValue) || actualValue < 0 || actualValue > 100) {
            showInputError(actualInput, '实际进度必须是0-100之间的数字');
            isValid = false;
        }
    }
    
    return isValid;
}

/**
 * 初始化资质证书表单
 */
function initializeQualificationForm() {
    const qualificationForm = document.getElementById('qualificationForm');
    if (!qualificationForm) return;
    
    // 为表单添加提交事件监听器
    qualificationForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // 验证表单
        if (!validateQualificationForm(qualificationForm)) {
            return;
        }
        
        // 获取表单数据
        const formData = new FormData(qualificationForm);
        const jsonData = formDataToJson(formData);
        
        // 确定请求URL和方法
        const qualificationId = qualificationForm.getAttribute('data-qualification-id');
        const url = qualificationId ? `/api/qualifications/${qualificationId}` : '/api/qualifications';
        const method = qualificationId ? 'PUT' : 'POST';
        
        // 发送AJAX请求
        sendAjaxRequest(
            url,
            method,
            jsonData,
            function(response) {
                // 成功处理
                showSuccessMessage('资质证书保存成功');
                
                // 如果是添加操作，重置表单
                if (!qualificationId) {
                    qualificationForm.reset();
                }
                
                // 如果设置了回调函数，调用它
                if (typeof qualificationFormCallback === 'function') {
                    qualificationFormCallback(response);
                }
            },
            function(status, error) {
                // 错误处理
                showErrorMessage('保存资质证书失败：' + (error.message || '未知错误'));
            }
        );
    });
}

/**
 * 验证资质证书表单
 * @param {HTMLFormElement} form 表单元素
 * @returns {boolean} 验证结果
 */
function validateQualificationForm(form) {
    let isValid = true;
    
    // 验证工人ID
    const workerIdInput = form.querySelector('[name="workerId"]');
    if (workerIdInput && !workerIdInput.value.trim()) {
        showInputError(workerIdInput, '请选择工人');
        isValid = false;
    }
    
    // 验证证书类型
    const typeInput = form.querySelector('[name="type"]');
    if (typeInput && !typeInput.value.trim()) {
        showInputError(typeInput, '证书类型不能为空');
        isValid = false;
    }
    
    // 验证证书编号
    const numberInput = form.querySelector('[name="number"]');
    if (numberInput && !numberInput.value.trim()) {
        showInputError(numberInput, '证书编号不能为空');
        isValid = false;
    }
    
    // 验证有效期
    const expiryDateInput = form.querySelector('[name="expiryDate"]');
    if (expiryDateInput && !expiryDateInput.value.trim()) {
        showInputError(expiryDateInput, '请选择有效期');
        isValid = false;
    }
    
    return isValid;
}

/**
 * 显示输入框错误提示
 * @param {HTMLInputElement} input 输入框元素
 * @param {string} message 错误信息
 */
function showInputError(input, message) {
    // 清除之前的错误
    clearInputError(input);
    
    // 添加错误类
    input.classList.add('is-invalid');
    
    // 创建错误提示元素
    const errorDiv = document.createElement('div');
    errorDiv.className = 'invalid-feedback';
    errorDiv.textContent = message;
    
    // 添加到输入框后面
    input.parentNode.appendChild(errorDiv);
}

/**
 * 清除输入框错误提示
 * @param {HTMLInputElement} input 输入框元素
 */
function clearInputError(input) {
    // 移除错误类
    input.classList.remove('is-invalid');
    
    // 移除错误提示元素
    const errorDiv = input.parentNode.querySelector('.invalid-feedback');
    if (errorDiv) {
        errorDiv.remove();
    }
}

/**
 * 显示成功消息
 * @param {string} message 消息内容
 */
function showSuccessMessage(message) {
    showMessage(message, 'success');
}

/**
 * 显示错误消息
 * @param {string} message 消息内容
 */
function showErrorMessage(message) {
    showMessage(message, 'danger');
}

/**
 * 显示提示消息
 * @param {string} message 消息内容
 * @param {string} type 消息类型：success, info, warning, danger
 */
function showMessage(message, type = 'info') {
    // 查找消息容器
    let alertContainer = document.getElementById('alertContainer');
    
    // 如果不存在，则创建一个
    if (!alertContainer) {
        alertContainer = document.createElement('div');
        alertContainer.id = 'alertContainer';
        alertContainer.className = 'alert-container';
        document.body.appendChild(alertContainer);
        
        // 添加样式
        const style = document.createElement('style');
        style.textContent = `
            .alert-container {
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 9999;
                max-width: 400px;
            }
            .alert-container .alert {
                margin-bottom: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            }
        `;
        document.head.appendChild(style);
    }
    
    // 创建提示框
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.role = 'alert';
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    
    // 添加到容器中
    alertContainer.appendChild(alert);
    
    // 添加点击事件关闭提示框
    const closeButton = alert.querySelector('.btn-close');
    if (closeButton) {
        closeButton.addEventListener('click', function() {
            alert.remove();
        });
    }
    
    // 5秒后自动关闭
    setTimeout(function() {
        // 添加淡出动画
        alert.classList.remove('show');
        
        // 动画结束后移除元素
        setTimeout(function() {
            alert.remove();
        }, 150);
    }, 5000);
}

/**
 * 将FormData对象转换为JSON对象
 * @param {FormData} formData 表单数据
 * @returns {Object} JSON对象
 */
function formDataToJson(formData) {
    const jsonData = {};
    
    // 遍历所有表单字段
    for (const [key, value] of formData.entries()) {
        // 检查是否已经存在该字段
        if (jsonData[key]) {
            // 如果已经存在且不是数组，转换为数组
            if (!Array.isArray(jsonData[key])) {
                jsonData[key] = [jsonData[key]];
            }
            
            // 添加新值
            jsonData[key].push(value);
        } else {
            // 不存在则直接赋值
            jsonData[key] = value;
        }
    }
    
    return jsonData;
} 