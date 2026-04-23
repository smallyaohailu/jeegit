package io.jeegit.business.matter;

import io.jeegit.common.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MatterService {

    private final MatterRepository repository;

    public MatterService(MatterRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Matter submit(String title, String category, String description, String applicantId) {
        Matter m = new Matter(
                UUID.randomUUID().toString(),
                TenantContext.tenant(),
                title,
                category,
                description,
                applicantId
        );
        return repository.save(m);
    }

    @Transactional(readOnly = true)
    public Matter get(String id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("matter not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Matter> listForCurrentTenant() {
        return repository.findByTenantIdOrderByCreatedAtDesc(TenantContext.tenant());
    }

    @Transactional
    public Matter assignDepartment(String id, String department, String status) {
        Matter m = get(id);
        m.setAssignedDepartment(department);
        if (status != null) {
            m.setStatus(status);
        }
        return repository.save(m);
    }
}
