/**
 * ResearchSphere client-side validation and charts
 */
document.addEventListener('DOMContentLoaded', function () {
    initFormValidation();
    initCharts();
});

function initFormValidation() {
    document.querySelectorAll('form[data-validate="true"]').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            var required = form.querySelectorAll('[required]');
            var valid = true;
            required.forEach(function (field) {
                if (!field.value || !field.value.trim()) {
                    valid = false;
                    field.classList.add('is-invalid');
                } else {
                    field.classList.remove('is-invalid');
                }
            });
            var pass = form.querySelector('#password');
            var confirm = form.querySelector('#confirmPassword');
            if (pass && confirm && pass.value !== confirm.value) {
                valid = false;
                confirm.classList.add('is-invalid');
                alert('Passwords do not match.');
            }
            if (!valid) e.preventDefault();
        });
    });
}

function initCharts() {
    var taskCanvas = document.getElementById('taskChart');
    var projectCanvas = document.getElementById('projectChart');
    if (!taskCanvas || typeof Chart === 'undefined') return;

    fetch(contextPath + '/app/api/charts/tasks')
        .then(function (r) { return r.json(); })
        .then(function (data) {
            new Chart(taskCanvas, {
                type: 'doughnut',
                data: {
                    labels: data.labels,
                    datasets: [{ data: data.values, backgroundColor: ['#6366f1', '#06b6d4', '#10b981'] }]
                },
                options: { responsive: true, plugins: { legend: { position: 'bottom' } } }
            });
        });

    if (projectCanvas) {
        fetch(contextPath + '/app/api/charts/projects')
            .then(function (r) { return r.json(); })
            .then(function (data) {
                new Chart(projectCanvas, {
                    type: 'bar',
                    data: {
                        labels: data.labels,
                        datasets: [{ label: 'Projects', data: data.values, backgroundColor: '#4f46e5' }]
                    },
                    options: { responsive: true, scales: { y: { beginAtZero: true } } }
                });
            });
    }
}

function confirmDelete(message) {
    return confirm(message || 'Are you sure you want to delete?');
}
