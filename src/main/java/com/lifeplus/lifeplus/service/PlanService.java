package com.lifeplus.lifeplus.service;

import com.google.firebase.messaging.*;
import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.model.form.*;
import com.lifeplus.lifeplus.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Manuel Pedrozo
 */
@Service
public class PlanService {

    private PlanRepository planRepository;
    private PlanTemplateRepository planTemplateRepository;
    private RoutineRepository routineRepository;
    private RoutineDivisionRepository routineDivisionRepository;
    private ExerciseRepository exerciseRepository;
    private AssignedExerciseRepository assignedExerciseRepository;
    private TeamUserRepository teamUserRepository;
    private UserService userService;
    private PatientService patientService;
    private RegistrationTokenRepository registrationTokenRepository;

    @Autowired
    public PlanService(
            PlanRepository planRepository,
            PlanTemplateRepository planTemplateRepository,
            RoutineRepository routineRepository,
            RoutineDivisionRepository routineDivisionRepository,
            ExerciseRepository exerciseRepository,
            AssignedExerciseRepository assignedExerciseRepository,
            UserService userService,
            PatientService patientService,
            RegistrationTokenRepository registrationTokenRepository,
            TeamUserRepository teamUserRepository) {
        this.planRepository = planRepository;
        this.planTemplateRepository = planTemplateRepository;
        this.routineRepository = routineRepository;
        this.routineDivisionRepository = routineDivisionRepository;
        this.exerciseRepository = exerciseRepository;
        this.assignedExerciseRepository = assignedExerciseRepository;
        this.userService = userService;
        this.patientService = patientService;
        this.teamUserRepository = teamUserRepository;
        this.registrationTokenRepository = registrationTokenRepository;
    }

    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    public Optional<Plan> findById(int id) {
        return planRepository.findById(id);
    }

    public Optional<Plan> findByPatientId(int id) {
        return planRepository.findByPatient_Id(id);
    }

    public List<PlanTemplate> findAllTemplates() {
        return planTemplateRepository.findAll();
    }

    public Optional<PlanTemplate> findTemplateById(int id) {
        return planTemplateRepository.findById(id);
    }

    public List<PlanTemplate> findAgentTemplates() {
        User creator = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        return planTemplateRepository.findAllByCreator_Id(creator.getId());
    }

    public Optional<Plan> findPatientPlan() {
        Patient patient = patientService.getCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        final Optional<Plan> optionalPlan = findByPatientId(patient.getId());
        return optionalPlan;
    }

    private Optional<Routine> findRoutineById(int id) {
        return routineRepository.findById(id);
    }

    public Optional<Routine> findRoutine(int planId, int routineId) {
        Plan plan = findById(planId).orElseThrow(() -> new NoSuchElementException(String.format("Plan: %d does not exist.", planId)));
        return findRoutineFromList(routineId, plan.getRoutines());
    }

    public Optional<Routine> findTemplateRoutine(int templateId, int routineId) {
        PlanTemplate template = findTemplateById(templateId).orElseThrow(() -> new NoSuchElementException(String.format("Template: %d does not exist.", templateId)));
        return findRoutineFromList(routineId, template.getRoutines());
    }

    private Optional<Routine> findRoutineFromList(int routineId, List<Routine> routines) {
        for (Routine r: routines) {
            if (r.getId() == routineId) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    private void checkCanModify(User agent, int patientUserId) {
        if (agent.getType() == UserType.ADMIN) return;
        Optional<TeamUser> teamUserOptional = teamUserRepository.findByUserIdAndPatientUserId(agent.getId(), patientUserId);
        if (teamUserOptional.isPresent()) {
            TeamUser teamUser = teamUserOptional.get();
            if (teamUser.getRole() == TeamRole.MODIFY || teamUser.getRole() == TeamRole.LEADER) {
                return;
            }
        }
        throw new BadCredentialsException("User does not have permission.");
    }

    public int save(PlanForm planForm) {
        Patient patient = patientService.findById(planForm.getPatient()).orElseThrow(() -> new NoSuchElementException(String.format("Patient: %d does not exist.", planForm.getPatient())));
        User current = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        checkCanModify(current, patient.getUser().getId());
        Plan plan = new Plan(patient);
        planRepository.save(plan);
        return plan.getId();
    }

    public void update(int id, PlanForm planForm) {
        Patient patient = patientService.findById(planForm.getPatient()).orElseThrow(() -> new NoSuchElementException(String.format("Patient: %d does not exist.", planForm.getPatient())));
        User current = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        checkCanModify(current, patient.getUser().getId());
        Plan plan = findById(id).orElseThrow(() -> new NoSuchElementException(String.format("Plan: %d does not exist.", id)));
        plan.setPatient(patient);
        planRepository.save(plan);
    }

    private AssignedExercise saveAssignedExercise(AssignedExerciseForm assignedExerciseForm, int order) {
        Exercise exercise = exerciseRepository.findById(assignedExerciseForm.getExercise()) .orElseThrow(() -> new NoSuchElementException(String.format("Exercise: %d does not exist.", assignedExerciseForm.getExercise())));
        AssignedExercise assignedExercise = new AssignedExercise(exercise, order, assignedExerciseForm.getDuration(), assignedExerciseForm.getRepetitions(), assignedExerciseForm.getSets());
        assignedExerciseRepository.save(assignedExercise);
        return assignedExercise;
    }

    private RoutineDivision saveDivision(RoutineDivisionForm divisionForm, int order) {
        RoutineDivision routineDivision = new RoutineDivision();
        routineDivision.setName(divisionForm.getName());
        routineDivision.setNumber(order);

        List<AssignedExercise> divisionExercises = new ArrayList<>();

        List<AssignedExerciseForm> assignedExerciseForms = divisionForm.getAssignedExercises();
        for (int i = 0; i < assignedExerciseForms.size(); i++) {
            divisionExercises.add(saveAssignedExercise(assignedExerciseForms.get(i), i));
        }
        routineDivision.setAssignedExercises(divisionExercises);

        routineDivisionRepository.save(routineDivision);
        return routineDivision;
    }

    public int addRoutine(int planId, RoutineForm routineForm) {
        Plan plan = findById(planId).orElseThrow(() -> new NoSuchElementException(String.format("Plan: %d does not exist.", planId)));
        User creator = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        checkCanModify(creator, plan.getPatient().getUser().getId());

        Routine routine = new Routine(routineForm);

        List<RoutineDivision> routineDivisions = new ArrayList<>();

        List<RoutineDivisionForm> divisionForms = routineForm.getDivisions();
        for (int i = 0; i < divisionForms.size(); i++) {
            routineDivisions.add(saveDivision(divisionForms.get(i), i));
        }
        routine.setDivisions(routineDivisions);
        routineRepository.save(routine);

        plan.getRoutines().add(routine);
        planRepository.save(plan);
        return routine.getId();
    }

    public void removeRoutine(int planId, int routineId) {
        Plan plan = findById(planId).orElseThrow(() -> new NoSuchElementException(String.format("Plan: %d does not exist.", planId)));
        User creator = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        checkCanModify(creator, plan.getPatient().getUser().getId());

        Routine routine = findRoutineById(routineId).orElseThrow(() -> new NoSuchElementException(String.format("Routine: %d does not exist.", routineId)));
        List<Routine> routines = plan.getRoutines();
        for (int i = 0; i < routines.size(); i++) {
            if (routines.get(i).getId() == routine.getId()) {
                routines.remove(i);
                break;
            }
        }
        routine.getDivisions()
                .forEach(d -> d.getAssignedExercises()
                        .forEach(assignedExercise -> {
                            assignedExercise.setActive(false);
                            assignedExerciseRepository.save(assignedExercise);
        }));
        planRepository.save(plan);
        routineRepository.delete(routine);
    }

    public void updateRoutine(int planId, int routineId, RoutineForm routineForm) {
        Plan plan = findById(planId).orElseThrow(() -> new NoSuchElementException(String.format("Plan: %d does not exist.", planId)));
        User creator = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        checkCanModify(creator, plan.getPatient().getUser().getId());

        // Make sure the routine actually belongs to the plan
        Routine routine = null;
        for (Routine planRoutine: plan.getRoutines()) {
            if (planRoutine.getId() == routineId) {
                routine = planRoutine;
                break;
            }
        }
        if (routine == null) {
            throw new NoSuchElementException(String.format("Routine: %d does not exist.", routineId));
        }

        routine.setName(routineForm.getName());
        routine.setDescription(routineForm.getDescription());
        routine.setFrequency(routineForm.getFrequency());
        routine.setFrequencyType(routineForm.getFrequencyType());
        routine.setBorg(routineForm.getBorg());

        Hibernate.initialize(routine.getDivisions()); // Eagerly initialize divisions, in case there are none
        List<RoutineDivision> oldRoutineDivisions = routine.getDivisions();
        List<RoutineDivision> newRoutineDivisions = new ArrayList<>();

        int divOrder = 0;
        for (RoutineDivisionForm routineDivisionForm: routineForm.getDivisions()) {

            if (routineDivisionForm.getId() == null) { // Did not exist.
                newRoutineDivisions.add(saveDivision(routineDivisionForm, divOrder++));
                continue;
            }

            boolean found = false;
            int routineDivisionId = routineDivisionForm.getId();
            for (RoutineDivision routineDivision: oldRoutineDivisions) {
                if (routineDivisionId == routineDivision.getId()) {
                    routineDivision.setName(routineDivisionForm.getName());
                    routineDivision.setNumber(divOrder++);

                    // Check every assigned exercise
                    List<AssignedExercise> oldAssignedExercises = routineDivision.getAssignedExercises();
                    oldAssignedExercises.forEach(ae -> ae.setActive(false));
                    List<AssignedExercise> newAssignedExercises = new ArrayList<>();
                    int aeOrder = 0;
                    for(AssignedExerciseForm assignedExerciseForm: routineDivisionForm.getAssignedExercises()) {
                        boolean changedOrNew = true;
                        for (AssignedExercise assignedExercise: oldAssignedExercises) {
                            if (assignedExerciseForm.getExercise() == assignedExercise.getExercise().getId()) {
                                if (assignedExerciseForm.equals(assignedExercise)) { // Has not changed
                                    assignedExercise.setActive(true);
                                    assignedExercise.setNumber(aeOrder++);
                                    newAssignedExercises.add(assignedExercise);
                                    changedOrNew = false;
                                    break;
                                }
                            }
                        }
                        if (changedOrNew) {
                            newAssignedExercises.add(saveAssignedExercise(assignedExerciseForm, aeOrder++));
                        }
                    }
                    // Delete assigned exercises not in form or updated
                    oldAssignedExercises.forEach(ae -> {
                        if (!ae.isActive())
                            assignedExerciseRepository.save(ae);
                    });

                    routineDivision.setAssignedExercises(newAssignedExercises);
                    routineDivisionRepository.save(routineDivision);
                    newRoutineDivisions.add(routineDivision);


                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new NoSuchElementException(String.format("Routine division: %d does not exist.", routineDivisionId));
            }
        }

        routine.setDivisions(newRoutineDivisions);
        routineRepository.save(routine);

        // Delete divisions not in form
        oldRoutineDivisions.forEach(oldRoutineDivision -> {
            boolean toDelete = true;
            for (RoutineDivision routineDivision: newRoutineDivisions) {
                if (oldRoutineDivision.getId() == routineDivision.getId()) {
                    toDelete = false;
                    break;
                }
            }
            if (toDelete) {
                oldRoutineDivision.getAssignedExercises()
                        .forEach(assignedExercise -> {
                            assignedExercise.setActive(false);
                            assignedExerciseRepository.save(assignedExercise);
                        });
                routineDivisionRepository.delete(oldRoutineDivision);
            }
        });

        planRepository.save(plan);

        int userId = plan.getPatient().getUser().getId();
        registrationTokenRepository.findById(userId).ifPresent(token -> notifyUser(token.getToken()));
    }

    private void delete(Plan plan) {
        User creator = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        checkCanModify(creator, plan.getPatient().getUser().getId());
        plan.getRoutines().forEach(r -> r.getDivisions().forEach(d -> d.getAssignedExercises().forEach(ae -> {
            ae.setActive(false);
            assignedExerciseRepository.save(ae);
        })));
        planRepository.delete(plan);
    }

    public void delete(int id) {
        Plan plan = findById(id).orElseThrow(() -> new NoSuchElementException(String.format("Plan: %d does not exist.", id)));
        delete(plan);
    }

    public int saveTemplate(PlanTemplateForm planTemplateForm) {
        User creator = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        PlanTemplate template = new PlanTemplate(planTemplateForm.getName(), creator);
        planTemplateRepository.save(template);
        return template.getId();
    }

    public void updateTemplate(int id, PlanTemplateForm planTemplateForm) {
        User creator = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        PlanTemplate template = findTemplateById(id).orElseThrow(() -> new NoSuchElementException(String.format("Template: %d does not exist.", id)));
        template.setCreator(creator);
        template.setName(planTemplateForm.getName());
        planTemplateRepository.save(template);
    }

    public void deleteTemplate(int id) {
        PlanTemplate template = findTemplateById(id).orElseThrow(() -> new NoSuchElementException(String.format("Template: %d does not exist.", id)));
        planTemplateRepository.delete(template);
        template.getRoutines()
                .forEach(r -> r.getDivisions()
                .forEach(d -> d.getAssignedExercises()
                .forEach(ae -> assignedExerciseRepository.delete(ae))));
    }

    public int addTemplateRoutine(int templateId, RoutineForm routineForm) {
        PlanTemplate template = findTemplateById(templateId).orElseThrow(() -> new NoSuchElementException(String.format("Plan: %d does not exist.", templateId)));
        return addTemplateRoutine(template, routineForm);
    }

    private int addTemplateRoutine(PlanTemplate template, RoutineForm routineForm) {
        Routine routine = new Routine(routineForm);

        List<RoutineDivision> routineDivisions = new ArrayList<>();

        List<RoutineDivisionForm> divisionForms = routineForm.getDivisions();
        for (int i = 0; i < divisionForms.size(); i++) {
            routineDivisions.add(saveDivision(divisionForms.get(i), i));
        }
        routine.setDivisions(routineDivisions);
        routineRepository.save(routine);

        template.getRoutines().add(routine);
        planTemplateRepository.save(template);
        return routine.getId();
    }

    public void updateTemplateRoutine(int templateId, int routineId, RoutineForm routineForm) {
        PlanTemplate template = findTemplateById(templateId).orElseThrow(() -> new NoSuchElementException(String.format("Template: %d does not exist.", templateId)));
        removeTemplateRoutine(template, routineId);
        addTemplateRoutine(template, routineForm);
    }

    private void removeTemplateRoutine(PlanTemplate template, int routineId) {
        Routine routine = null;
        List<Routine> routines = template.getRoutines();
        for (int i = 0; i < routines.size(); i++) {
            if (routines.get(i).getId() == routineId) {
                routine = routines.get(i);
                routines.remove(i);
                break;
            }
        }
        if (routine == null) {
            throw new NoSuchElementException(String.format("Routine: %d does not exist.", routineId));
        }

        planTemplateRepository.save(template);
        routineRepository.delete(routine);

        routine.getDivisions()
                .forEach(d -> d.getAssignedExercises()
                .forEach(ae -> assignedExerciseRepository.delete(ae)));
    }

    public void removeTemplateRoutine(int templateId, int routineId) {
        PlanTemplate template = findTemplateById(templateId).orElseThrow(() -> new NoSuchElementException(String.format("Template: %d does not exist.", templateId)));
        removeTemplateRoutine(template, routineId);
    }

    public int applyTemplate(ApplyTemplateForm applyTemplateForm) {
        PlanTemplate template = findTemplateById(applyTemplateForm.getTemplate()).orElseThrow(() -> new NoSuchElementException(String.format("Template: %d does not exist.", applyTemplateForm.getTemplate())));
        Patient patient = patientService.findById(applyTemplateForm.getPatient()).orElseThrow(() -> new NoSuchElementException(String.format("Patient: %d does not exist.", applyTemplateForm.getPatient())));
        User current = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        checkCanModify(current, patient.getUser().getId());

        planRepository.findByPatient_Id(patient.getId()).ifPresent(this::delete);

        Plan plan = new Plan(patient);

        List<Routine> routines = new ArrayList<>();
        template.getRoutines().forEach(r -> {
            Routine routine = Routine.copy(r);
            List<RoutineDivision> divisions = new ArrayList<>();
            r.getDivisions().forEach(d -> {
                RoutineDivision division = RoutineDivision.copy(d);
                List<AssignedExercise> assignedExercises = new ArrayList<>();
                d.getAssignedExercises().forEach(ae -> {
                    AssignedExercise assignedExercise = AssignedExercise.copy(ae);
                    assignedExerciseRepository.save(assignedExercise);
                    assignedExercises.add(assignedExercises.size(), assignedExercise);
                });
                division.setAssignedExercises(assignedExercises);
                routineDivisionRepository.save(division);
                divisions.add(divisions.size(), division);
            });
            routine.setDivisions(divisions);
            routineRepository.save(routine);
            routines.add(routines.size(), routine);
        });
        plan.setRoutines(routines);

        planRepository.save(plan);
        return plan.getId();
    }

    public int saveTemplateFromPlan(PlanTemplateForm planTemplateForm) {
        User creator = userService.findCurrent().orElseThrow(() -> new NoSuchElementException("User does not exist."));
        Plan plan = findById(planTemplateForm.getPlan()).orElseThrow(() -> new NoSuchElementException(String.format("Plan: %d does not exist.", planTemplateForm.getPlan())));

        PlanTemplate template = new PlanTemplate(planTemplateForm.getName(), creator);

        List<Routine> routines = new ArrayList<>();
        plan.getRoutines().forEach(r -> {
            Routine routine = Routine.copy(r);
            List<RoutineDivision> divisions = new ArrayList<>();
            r.getDivisions().forEach(d -> {
                RoutineDivision division = RoutineDivision.copy(d);
                List<AssignedExercise> assignedExercises = new ArrayList<>();
                d.getAssignedExercises().forEach(ae -> {
                    AssignedExercise assignedExercise = AssignedExercise.copy(ae);
                    assignedExerciseRepository.save(assignedExercise);
                    assignedExercises.add(assignedExercises.size(), assignedExercise);
                });
                division.setAssignedExercises(assignedExercises);
                routineDivisionRepository.save(division);
                divisions.add(divisions.size(), division);
            });
            routine.setDivisions(divisions);
            routineRepository.save(routine);
            routines.add(routines.size(), routine);
        });
        template.setRoutines(routines);

        planTemplateRepository.save(template);
        return template.getId();
    }

    private void notifyUser(String registrationToken) {
        // This registration token comes from the client FCM SDKs.

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setNotification(new Notification(
                        "Cambios en la rutina",
                        "Tu rutina ha sido modificada. Vea los nuevos cambios."))
                .setToken(registrationToken)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.

        try {
            // Response is a message ID string.
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

}
