# Compose UI Patterns Quick Reference

Quick reference for Universal Password Manager Android Compose UI patterns during code reviews. For comprehensive details, read `docs/ARCHITECTURE.md` and `docs/STYLE_AND_BEST_PRACTICES.md`.

## Component Reuse

**✅ GOOD - Uses existing components**:
```kotlin
Universal Password ManagerButton(
    text = "Submit",
    onClick = onSubmit
)

Universal Password ManagerTextField(
    value = text,
    onValueChange = onTextChange,
    label = "Email"
)
```

**❌ BAD - Duplicates existing components**:
```kotlin
// ❌ Recreating Universal Password ManagerButton
Button(
    onClick = onSubmit,
    colors = ButtonDefaults.buttonColors(
        containerColor = Universal Password ManagerTheme.colorScheme.primary
    )
) {
    Text("Submit")
}
```

**Key Rules**:
- Check `:ui` module for existing components before creating custom ones
- Use Universal Password ManagerButton, Universal Password ManagerTextField, etc. for consistency
- Place new reusable components in `:ui` module

---

## Theme Usage

**✅ GOOD - Uses theme**:
```kotlin
Text(
    text = "Title",
    style = Universal Password ManagerTheme.typography.titleLarge,
    color = Universal Password ManagerTheme.colorScheme.primary
)

Spacer(modifier = Modifier.height(16.dp))  // Standard spacing
```

**❌ BAD - Hardcoded values**:
```kotlin
Text(
    text = "Title",
    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),  // Use theme
    color = Color(0xFF0066FF)  // Use theme color
)

Spacer(modifier = Modifier.height(17.dp))  // Non-standard spacing
```

**Key Rules**:
- Use `Universal Password ManagerTheme.colorScheme` for colors
- Use `Universal Password ManagerTheme.typography` for text styles
- Use standard spacing (4.dp, 8.dp, 16.dp, 24.dp)

---

## Quick Checklist

### UI Patterns
- [ ] Using existing Universal Password Manager components from `:ui` module?
- [ ] Using Universal Password ManagerTheme for colors and typography?
- [ ] Using standard spacing values (4, 8, 16, 24 dp)?
- [ ] No hardcoded colors or text styles?
- [ ] UI is stateless (observes state, doesn't modify)?

---

For comprehensive details, always refer to:
- `docs/ARCHITECTURE.md` - Full architecture patterns
- `docs/STYLE_AND_BEST_PRACTICES.md` - Complete style guide
