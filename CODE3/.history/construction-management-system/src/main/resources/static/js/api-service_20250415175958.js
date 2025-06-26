/**
 * API服务模块
 * 用于处理与后端API的通信
 */

/**
 * 发送AJAX请求
 * @param {string} url 请求URL
 * @param {string} method 请求方法：GET, POST, PUT, DELETE等
 * @param {Object} data 请求数据
 * @param {Function} successCallback 成功回调函数
 * @param {Function} errorCallback 错误回调函数
 */
function sendAjaxRequest(url, method, data, successCallback, errorCallback) {
    // 创建请求选项
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        },
        credentials: 'same-origin' // 包含跨域请求的cookies
    };
    
    // 添加请求体（如果有）
    if (data && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(data);
    }
    
    // 发送请求
    fetch(url, options)
        .then(response => {
            // 检查响应状态
            if (!response.ok) {
                // 如果响应不成功，抛出错误
                return response.json().then(errData => {
                    throw {
                        status: response.status,
                        message: errData.message || '请求失败'
                    };
                });
            }
            
            // 检查内容类型
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                // 如果是JSON，解析它
                return response.json();
            } else {
                // 否则返回文本
                return response.text();
            }
        })
        .then(data => {
            // 调用成功回调
            if (typeof successCallback === 'function') {
                successCallback(data);
            }
        })
        .catch(error => {
            // 调用错误回调
            if (typeof errorCallback === 'function') {
                errorCallback(error.status || 500, error);
            } else {
                console.error('请求错误:', error);
            }
        });
}

/**
 * 工人相关API服务
 */
const WorkerService = {
    /**
     * 获取所有工人列表
     * @param {Function} callback 回调函数
     * @param {Object} params 查询参数
     */
    getAllWorkers: function(callback, params = {}) {
        // 构建查询字符串
        const queryString = Object.keys(params)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join('&');
        
        const url = `/api/workers${queryString ? '?' + queryString : ''}`;
        
        sendAjaxRequest(
            url,
            'GET',
            null,
            callback,
            function(status, error) {
                console.error('获取工人列表失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 获取工人详情
     * @param {number} workerId 工人ID
     * @param {Function} callback 回调函数
     */
    getWorkerById: function(workerId, callback) {
        sendAjaxRequest(
            `/api/workers/${workerId}`,
            'GET',
            null,
            callback,
            function(status, error) {
                console.error(`获取工人(ID=${workerId})详情失败:`, error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 创建新工人
     * @param {Object} workerData 工人数据
     * @param {Function} callback 回调函数
     */
    createWorker: function(workerData, callback) {
        sendAjaxRequest(
            '/api/workers',
            'POST',
            workerData,
            callback,
            function(status, error) {
                console.error('创建工人失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 更新工人信息
     * @param {number} workerId 工人ID
     * @param {Object} workerData 工人数据
     * @param {Function} callback 回调函数
     */
    updateWorker: function(workerId, workerData, callback) {
        sendAjaxRequest(
            `/api/workers/${workerId}`,
            'PUT',
            workerData,
            callback,
            function(status, error) {
                console.error(`更新工人(ID=${workerId})信息失败:`, error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 删除工人
     * @param {number} workerId 工人ID
     * @param {Function} callback 回调函数
     */
    deleteWorker: function(workerId, callback) {
        sendAjaxRequest(
            `/api/workers/${workerId}`,
            'DELETE',
            null,
            callback,
            function(status, error) {
                console.error(`删除工人(ID=${workerId})失败:`, error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 获取工人统计数据
     * @param {Function} callback 回调函数
     */
    getWorkerStatistics: function(callback) {
        sendAjaxRequest(
            '/api/statistics/workers',
            'GET',
            null,
            callback,
            function(status, error) {
                console.error('获取工人统计数据失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    }
};

/**
 * 考勤相关API服务
 */
const AttendanceService = {
    /**
     * 获取考勤记录列表
     * @param {Function} callback 回调函数
     * @param {Object} params 查询参数
     */
    getAttendanceRecords: function(callback, params = {}) {
        // 构建查询字符串
        const queryString = Object.keys(params)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join('&');
        
        const url = `/api/attendance${queryString ? '?' + queryString : ''}`;
        
        sendAjaxRequest(
            url,
            'GET',
            null,
            callback,
            function(status, error) {
                console.error('获取考勤记录失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 创建考勤记录
     * @param {Object} attendanceData 考勤数据
     * @param {Function} callback 回调函数
     */
    createAttendanceRecord: function(attendanceData, callback) {
        sendAjaxRequest(
            '/api/attendance',
            'POST',
            attendanceData,
            callback,
            function(status, error) {
                console.error('创建考勤记录失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 获取考勤统计数据
     * @param {Object} params 查询参数（日期范围等）
     * @param {Function} callback 回调函数
     */
    getAttendanceStatistics: function(params, callback) {
        // 构建查询字符串
        const queryString = Object.keys(params)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join('&');
        
        const url = `/api/statistics/attendance${queryString ? '?' + queryString : ''}`;
        
        sendAjaxRequest(
            url,
            'GET',
            null,
            callback,
            function(status, error) {
                console.error('获取考勤统计数据失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    }
};

/**
 * 安全培训相关API服务
 */
const SafetyTrainingService = {
    /**
     * 获取安全培训记录列表
     * @param {Function} callback 回调函数
     * @param {Object} params 查询参数
     */
    getSafetyTrainings: function(callback, params = {}) {
        // 构建查询字符串
        const queryString = Object.keys(params)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join('&');
        
        const url = `/api/safety-trainings${queryString ? '?' + queryString : ''}`;
        
        sendAjaxRequest(
            url,
            'GET',
            null,
            callback,
            function(status, error) {
                console.error('获取安全培训记录失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 创建安全培训记录
     * @param {Object} trainingData 培训数据
     * @param {Function} callback 回调函数
     */
    createSafetyTraining: function(trainingData, callback) {
        sendAjaxRequest(
            '/api/safety-trainings',
            'POST',
            trainingData,
            callback,
            function(status, error) {
                console.error('创建安全培训记录失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 获取安全事故统计数据
     * @param {Object} params 查询参数（日期范围等）
     * @param {Function} callback 回调函数
     */
    getSafetyIncidentStatistics: function(params, callback) {
        // 构建查询字符串
        const queryString = Object.keys(params)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join('&');
        
        const url = `/api/statistics/safety-incidents${queryString ? '?' + queryString : ''}`;
        
        sendAjaxRequest(
            url,
            'GET',
            null,
            callback,
            function(status, error) {
                console.error('获取安全事故统计数据失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    }
};

/**
 * 项目进度相关API服务
 */
const ProjectProgressService = {
    /**
     * 获取项目进度记录列表
     * @param {Function} callback 回调函数
     * @param {Object} params 查询参数
     */
    getProjectProgress: function(callback, params = {}) {
        // 构建查询字符串
        const queryString = Object.keys(params)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join('&');
        
        const url = `/api/progress${queryString ? '?' + queryString : ''}`;
        
        sendAjaxRequest(
            url,
            'GET',
            null,
            callback,
            function(status, error) {
                console.error('获取项目进度记录失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 创建项目进度记录
     * @param {Object} progressData 进度数据
     * @param {Function} callback 回调函数
     */
    createProjectProgress: function(progressData, callback) {
        sendAjaxRequest(
            '/api/progress',
            'POST',
            progressData,
            callback,
            function(status, error) {
                console.error('创建项目进度记录失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 更新项目进度记录
     * @param {number} progressId 进度记录ID
     * @param {Object} progressData 进度数据
     * @param {Function} callback 回调函数
     */
    updateProjectProgress: function(progressId, progressData, callback) {
        sendAjaxRequest(
            `/api/progress/${progressId}`,
            'PUT',
            progressData,
            callback,
            function(status, error) {
                console.error(`更新项目进度记录(ID=${progressId})失败:`, error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    }
};

/**
 * 资质证书相关API服务
 */
const QualificationService = {
    /**
     * 获取资质证书列表
     * @param {Function} callback 回调函数
     * @param {Object} params 查询参数
     */
    getQualifications: function(callback, params = {}) {
        // 构建查询字符串
        const queryString = Object.keys(params)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join('&');
        
        const url = `/api/qualifications${queryString ? '?' + queryString : ''}`;
        
        sendAjaxRequest(
            url,
            'GET',
            null,
            callback,
            function(status, error) {
                console.error('获取资质证书列表失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 获取单个资质证书详情
     * @param {number} qualificationId 资质证书ID
     * @param {Function} callback 回调函数
     */
    getQualificationById: function(qualificationId, callback) {
        sendAjaxRequest(
            `/api/qualifications/${qualificationId}`,
            'GET',
            null,
            callback,
            function(status, error) {
                console.error(`获取资质证书(ID=${qualificationId})详情失败:`, error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 创建资质证书
     * @param {Object} qualificationData 资质证书数据
     * @param {Function} callback 回调函数
     */
    createQualification: function(qualificationData, callback) {
        sendAjaxRequest(
            '/api/qualifications',
            'POST',
            qualificationData,
            callback,
            function(status, error) {
                console.error('创建资质证书失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 更新资质证书
     * @param {number} qualificationId 资质证书ID
     * @param {Object} qualificationData 资质证书数据
     * @param {Function} callback 回调函数
     */
    updateQualification: function(qualificationId, qualificationData, callback) {
        sendAjaxRequest(
            `/api/qualifications/${qualificationId}`,
            'PUT',
            qualificationData,
            callback,
            function(status, error) {
                console.error(`更新资质证书(ID=${qualificationId})失败:`, error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 删除资质证书
     * @param {number} qualificationId 资质证书ID
     * @param {Function} callback 回调函数
     */
    deleteQualification: function(qualificationId, callback) {
        sendAjaxRequest(
            `/api/qualifications/${qualificationId}`,
            'DELETE',
            null,
            callback,
            function(status, error) {
                console.error(`删除资质证书(ID=${qualificationId})失败:`, error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    },
    
    /**
     * 获取资质证书分布统计
     * @param {Function} callback 回调函数
     */
    getQualificationDistribution: function(callback) {
        sendAjaxRequest(
            '/api/statistics/qualifications/distribution',
            'GET',
            null,
            callback,
            function(status, error) {
                console.error('获取资质证书分布统计失败:', error);
                if (typeof callback === 'function') {
                    callback(null, error);
                }
            }
        );
    }
}; 