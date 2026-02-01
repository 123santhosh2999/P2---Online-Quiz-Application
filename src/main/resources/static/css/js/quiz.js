// Quiz Application JavaScript


// Global variables
let currentQuestion = 0;
let totalQuestions = 0;
let timerInterval;
let timeRemaining = 0;

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    initializeQuizTaking();
    initializeDashboard();
    initializeSearch();
});
const usernameRegex = /^[a-zA-Z0-9_.]+$/;

if (!usernameRegex.test(username)) {
  setError("Username must not contain spaces");
  return;
}

// Quiz Taking Functionality
function initializeQuizTaking() {
    if (!document.getElementById('quizForm')) return;
    
    totalQuestions = document.querySelectorAll('.question-container').length;
    
    // Initialize timer
    if (typeof timeLimit !== 'undefined' && timeLimit !== null) {
        timeRemaining = timeLimit * 60; // Convert to seconds
        startTimer();
    }
    
    // Setup navigation
    setupQuestionNavigation();
    updateProgressBar();
    updateNavigationButtons();

    // Restore previously selected answers (if any)
    loadAnswersFromStorage();
    updateQuestionNavigation();
    
    // Auto-save answers
    setupAutoSave();
}

function setupQuestionNavigation() {
    // Previous button
    const prevBtn = document.getElementById('prevBtn');
    if (prevBtn) {
        prevBtn.addEventListener('click', function() {
            if (currentQuestion > 0) {
                showQuestion(currentQuestion - 1);
            }
        });
    }
    
    // Next button
    const nextBtn = document.getElementById('nextBtn');
    if (nextBtn) {
        nextBtn.addEventListener('click', function() {
            if (currentQuestion < totalQuestions - 1) {
                showQuestion(currentQuestion + 1);
            }
        });
    }
    
    // Question navigation sidebar
    const navButtons = document.querySelectorAll('.question-nav-btn');
    navButtons.forEach(button => {
        button.addEventListener('click', function() {
            const questionIndex = parseInt(this.dataset.questionIndex);
            showQuestion(questionIndex);
        });
    });
    
    // Form submission
    const submitBtn = document.getElementById('submitBtn');
    if (submitBtn) {
        submitBtn.addEventListener('click', function(e) {
            e.preventDefault();
            showSubmissionConfirmation();
        });
    }
}

function showQuestion(questionIndex) {
    // Hide current question
    document.querySelectorAll('.question-container').forEach(container => {
        container.style.display = 'none';
    });
    
    // Show target question
    const targetQuestion = document.querySelector(`[data-question-index="${questionIndex}"]`);
    if (targetQuestion) {
        targetQuestion.style.display = 'block';
        currentQuestion = questionIndex;
        
        updateProgressBar();
        updateNavigationButtons();
        updateQuestionNavigation();
        
        // Scroll to top
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}

function updateProgressBar() {
    const progressBar = document.getElementById('progressBar');
    if (progressBar) {
        const progress = ((currentQuestion + 1) / totalQuestions) * 100;
        progressBar.style.width = progress + '%';
        progressBar.textContent = `Question ${currentQuestion + 1} of ${totalQuestions}`;
    }
}

function updateNavigationButtons() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const submitBtn = document.getElementById('submitBtn');
    
    // Show/hide previous button
    if (prevBtn) {
        prevBtn.style.display = currentQuestion > 0 ? 'block' : 'none';
    }
    
    // Show next or submit button
    if (currentQuestion === totalQuestions - 1) {
        if (nextBtn) nextBtn.style.display = 'none';
        if (submitBtn) submitBtn.style.display = 'block';
    } else {
        if (nextBtn) nextBtn.style.display = 'block';
        if (submitBtn) submitBtn.style.display = 'none';
    }
}

function updateQuestionNavigation() {
    // Update navigation buttons
    document.querySelectorAll('.question-nav-btn').forEach((btn, index) => {
        btn.classList.remove('current', 'answered');
        
        if (index === currentQuestion) {
            btn.classList.add('current');
        }
        
        // Check if question is answered
        const questionContainer = document.querySelector(`[data-question-index="${index}"]`);
        if (questionContainer) {
            const checkedInput = questionContainer.querySelector('input[type="radio"]:checked');
            if (checkedInput) {
                btn.classList.add('answered');
            }
        }
    });
}

function startTimer() {
    const timerElement = document.getElementById('timer');
    if (!timerElement) return;
    
    timerInterval = setInterval(function() {
        timeRemaining--;
        
        const minutes = Math.floor(timeRemaining / 60);
        const seconds = timeRemaining % 60;
        
        timerElement.textContent = 
            `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        
        // Warning at 5 minutes
        if (timeRemaining === 300) {
            showTimeWarning();
        }
        
        // Auto-submit when time is up
        if (timeRemaining <= 0) {
            autoSubmitQuiz();
        }
        
        // Add warning styling
        if (timeRemaining <= 300) {
            timerElement.parentElement.classList.add('timer-warning');
        }
    }, 1000);
}

function showTimeWarning() {
    const modal = new bootstrap.Modal(document.getElementById('timeWarningModal'));
    modal.show();
}

function autoSubmitQuiz() {
    clearInterval(timerInterval);
    
    // Show loading
    showLoadingOverlay('Time\'s up! Submitting your quiz...');
    
    // Submit form
    setTimeout(() => {
        document.getElementById('quizForm').submit();
    }, 2000);
}

function showSubmissionConfirmation() {
    const unansweredQuestions = getUnansweredQuestions();
    
    let message = 'Are you sure you want to submit your quiz?';
    if (unansweredQuestions.length > 0) {
        message += `\n\nYou have ${unansweredQuestions.length} unanswered questions: ${unansweredQuestions.join(', ')}`;
    }
    
    if (confirm(message)) {
        showLoadingOverlay('Submitting your quiz...');
        setTimeout(() => {
            document.getElementById('quizForm').submit();
        }, 1500);
    }
}

function getUnansweredQuestions() {
    const unanswered = [];
    
    document.querySelectorAll('.question-container').forEach((container, index) => {
        const checkedInput = container.querySelector('input[type="radio"]:checked');
        if (!checkedInput) {
            unanswered.push(index + 1);
        }
    });
    
    return unanswered;
}

function setupAutoSave() {
    // Save answers to localStorage periodically
    setInterval(function() {
        saveAnswersToStorage();
    }, 30000); // Every 30 seconds
    
    // Save on input change
    document.querySelectorAll('input[type="radio"]').forEach(input => {
        input.addEventListener('change', function() {
            saveAnswersToStorage();
            updateQuestionNavigation();
        });
    });
}

function saveAnswersToStorage() {
    const answers = {};
    
    document.querySelectorAll('input[type="radio"]:checked').forEach(input => {
        answers[input.name] = input.value;
    });
    
    localStorage.setItem('quiz_answers', JSON.stringify(answers));
}

function loadAnswersFromStorage() {
    const savedAnswers = localStorage.getItem('quiz_answers');
    if (savedAnswers) {
        const answers = JSON.parse(savedAnswers);
        
        Object.entries(answers).forEach(([name, value]) => {
            const input = document.querySelector(`input[name="${name}"][value="${value}"]`);
            if (input) {
                input.checked = true;
            }
        });
    }
}

// Dashboard Functionality
function initializeDashboard() {
    if (!document.getElementById('totalQuizzes')) return;
    
    // Load dashboard statistics
    loadDashboardStats();
    loadRecentActivity();
}

function loadDashboardStats() {
    // This would typically fetch from an API
    // For now, we'll simulate with static data
    setTimeout(() => {
        document.getElementById('totalQuizzes').textContent = '12';
        document.getElementById('completedQuizzes').textContent = '8';
        document.getElementById('averageScore').textContent = '78%';
        document.getElementById('bestScore').textContent = '95%';
    }, 500);
}

function loadRecentActivity() {
    const activityContainer = document.getElementById('recentActivity');
    if (!activityContainer) return;
    
    // Simulate loading recent activity
    setTimeout(() => {
        activityContainer.innerHTML = `
            <div class="list-group">
                <div class="list-group-item">
                    <div class="d-flex w-100 justify-content-between">
                        <h6 class="mb-1">JavaScript Fundamentals Quiz</h6>
                        <small>2 days ago</small>
                    </div>
                    <p class="mb-1">Score: 85/100 (85%)</p>
                </div>
                <div class="list-group-item">
                    <div class="d-flex w-100 justify-content-between">
                        <h6 class="mb-1">HTML & CSS Basics</h6>
                        <small>5 days ago</small>
                    </div>
                    <p class="mb-1">Score: 92/100 (92%)</p>
                </div>
                <div class="list-group-item">
                    <div class="d-flex w-100 justify-content-between">
                        <h6 class="mb-1">Database Concepts</h6>
                        <small>1 week ago</small>
                    </div>
                    <p class="mb-1">Score: 78/100 (78%)</p>
                </div>
            </div>
        `;
    }, 800);
}

// Search Functionality
function initializeSearch() {
    const searchInput = document.getElementById('searchInput');
    const searchBtn = document.getElementById('searchBtn');
    
    if (searchInput && searchBtn) {
        searchBtn.addEventListener('click', performSearch);
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                performSearch();
            }
        });
    }
}

function performSearch() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const quizCards = document.querySelectorAll('.quiz-card');
    
    quizCards.forEach(card => {
        const title = card.querySelector('.card-title').textContent.toLowerCase();
        const description = card.querySelector('.card-text').textContent.toLowerCase();
        
        if (title.includes(searchTerm) || description.includes(searchTerm)) {
            card.parentElement.style.display = 'block';
        } else {
            card.parentElement.style.display = 'none';
        }
    });
}

// Utility Functions
function showLoadingOverlay(message = 'Loading...') {
    const overlay = document.createElement('div');
    overlay.id = 'loadingOverlay';
    overlay.innerHTML = `
        <div class="d-flex flex-column align-items-center justify-content-center h-100">
            <div class="spinner-border text-primary mb-3" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="text-center">${message}</p>
        </div>
    `;
    overlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.9);
        z-index: 9999;
        backdrop-filter: blur(5px);
    `;
    
    document.body.appendChild(overlay);
}

function hideLoadingOverlay() {
    const overlay = document.getElementById('loadingOverlay');
    if (overlay) {
        overlay.remove();
    }
}

// Form Validation
function validateForm(form) {
    const inputs = form.querySelectorAll('input[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!input.value.trim()) {
            input.classList.add('is-invalid');
            isValid = false;
        } else {
            input.classList.remove('is-invalid');
            input.classList.add('is-valid');
        }
    });
    
    return isValid;
}

// Event Listeners for form validation
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form[data-validate]');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateForm(this)) {
                e.preventDefault();
                showAlert('Please fill in all required fields.', 'danger');
            }
        });
    });
});

function showAlert(message, type = 'info') {
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(alert, container.firstChild);
        
        // Auto-dismiss after 5 seconds
        setTimeout(() => {
            if (alert.parentNode) {
                alert.remove();
            }
        }, 5000);
    }
}

// Cleanup on page unload
window.addEventListener('beforeunload', function() {
    if (timerInterval) {
        clearInterval(timerInterval);
    }
    
    // Save current state
    if (document.getElementById('quizForm')) {
        saveAnswersToStorage();
    }
});
